package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.utils.HttpClientUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * LlmClient：通过 {@link HttpClientUtil} 调用大模型 HTTP 接口（非流式 / 流式 SSE）。
 */
@Service
public class LlmClient {

    private final String apiKey;
    private final String baseUrl;
    private final String model;
    /**
     * 是否在 content 为空时回退到 reasoning_content
     */
    private final boolean includeReasoningContent;

    private final AtomicInteger tokensUsed = new AtomicInteger(0);

    public LlmClient(
            @Value("${llm.apiKey}") String apiKey,
            @Value("${llm.endpoint}") String baseUrl,
            @Value("${llm.provider}") String model,
            @Value("${llm.include-reasoning-content:false}") boolean includeReasoningContent) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
        this.includeReasoningContent = includeReasoningContent;
    }

    /**
     * 非流式生成：返回完整文本
     */
    public String generate(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "你是一个友好的AI助手"),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.6
        );
        Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);

        try {
            HttpClientUtil.HttpResult r = HttpClientUtil.postJson(baseUrl, body, headers);
            if (!r.is2xx()) {
                throw new BaseException("LLM HTTP " + r.statusCode() + ": " + truncate(r.body()));
            }
            if (r.body() == null || r.body().isBlank()) {
                return "抱歉，我暂时没想好该怎么回答。";
            }
            Map<String, Object> result = JSON.parseObject(r.body(), new TypeReference<Map<String, Object>>() {
            });
            if (result == null) {
                return "抱歉，我暂时没想好该怎么回答。";
            }
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) (List<?>) result.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> first = choices.get(0);
                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) first.get("message");
                @SuppressWarnings("unchecked")
                Map<String, Object> usage = (Map<String, Object>) result.get("usage");
                if (usage != null && usage.get("total_tokens") != null) {
                    tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                }
                return assistantMessageText(message, includeReasoningContent);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
        return "抱歉，我暂时没想好该怎么回答。";
    }

    private static String truncate(String s) {
        if (s == null) {
            return "";
        }
        return s.length() > 500 ? s.substring(0, 500) + "…" : s;
    }

    /**
     * 流式生成：逐段回调片段；返回最终拼接文本
     */
    public String generateStream(String prompt, Consumer<String> onDelta) throws IOException {
        Map<String, Object> streamBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "你是一个友好的AI助手"),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.6,
                "stream", true
        );
        Map<String, String> headers = Map.of("Authorization", "Bearer " + apiKey);

        final InputStream in;
        try {
            in = HttpClientUtil.postJsonStream(baseUrl, streamBody, headers);
        } catch (UncheckedIOException e) {
            if (e.getCause() instanceof IOException ioe) {
                throw ioe;
            }
            throw new IOException(e.getMessage(), e.getCause() != null ? e.getCause() : e);
        } catch (HttpClientUtil.HttpClientException e) {
            throw new IOException(e.getMessage(), e);
        }

        StringBuilder sb = new StringBuilder();
        try (in;
             BufferedReader rd = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = rd.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String piece = parseDelta(line);
                if (!piece.isEmpty()) {
                    sb.append(piece);
                    if (onDelta != null) {
                        onDelta.accept(piece);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 非流式 message 对象：优先正文 {@code content}；{@code reasoning_content} 受配置开关控制。
     */
    private static String assistantMessageText(Map<String, Object> message, boolean includeReasoning) {
        if (message == null) {
            return "";
        }
        String c = scalarToText(message.get("content"));
        if (!c.isEmpty()) {
            return c;
        }
        if (!includeReasoning) {
            return "";
        }
        return scalarToText(message.get("reasoning_content"));
    }

    /**
     * 流式 delta：合并可展示片段；绝不回传整行 {@code data: {...}} JSON。
     */
    private static String deltaToText(Map<String, Object> delta, boolean includeReasoning) {
        if (delta == null || delta.isEmpty()) {
            return "";
        }
        String c = scalarToText(delta.get("content"));
        if (!c.isEmpty()) {
            return c;
        }
        if (!includeReasoning) {
            return "";
        }
        return scalarToText(delta.get("reasoning_content"));
    }

    private static String scalarToText(Object v) {
        if (v == null) {
            return "";
        }
        String s = String.valueOf(v);
        return "null".equals(s) ? "" : s;
    }

    @SuppressWarnings("unchecked")
    private String parseDelta(String line) {
        try {
            String s = line.startsWith("data:") ? line.substring(5).trim() : line;
            if (s.equals("[DONE]")) {
                return "";
            }
            if (s.startsWith("{")) {
                Map<String, Object> m = JSON.parseObject(s, new TypeReference<Map<String, Object>>() {
                });
                if (m == null) {
                    return "";
                }
                List<Map<String, Object>> choices = (List<Map<String, Object>>) (List<?>) m.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> first = choices.get(0);
                    Map<String, Object> delta = (Map<String, Object>) first.get("delta");
                    String fromDelta = deltaToText(delta, includeReasoningContent);
                    if (!fromDelta.isEmpty()) {
                        return fromDelta;
                    }
                    Map<String, Object> message = (Map<String, Object>) first.get("message");
                    if (message != null) {
                        String fromMsg = assistantMessageText(message, includeReasoningContent);
                        if (!fromMsg.isEmpty()) {
                            return fromMsg;
                        }
                    }
                }
                Map<String, Object> usage = (Map<String, Object>) m.get("usage");
                if (usage != null && usage.get("total_tokens") != null) {
                    tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                }
                return "";
            }
        } catch (Exception ignore) {
            // 非 JSON 行忽略
        }
        return "";
    }

    public int tokensUsed() {
        return tokensUsed.get();
    }
}
