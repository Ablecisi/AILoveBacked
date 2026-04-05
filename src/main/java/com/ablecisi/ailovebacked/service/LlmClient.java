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

    private final AtomicInteger tokensUsed = new AtomicInteger(0);

    public LlmClient(
            @Value("${llm.apiKey}") String apiKey,
            @Value("${llm.endpoint}") String baseUrl,
            @Value("${llm.provider}") String model) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
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
                Object content = message == null ? null : message.get("content");
                @SuppressWarnings("unchecked")
                Map<String, Object> usage = (Map<String, Object>) result.get("usage");
                if (usage != null && usage.get("total_tokens") != null) {
                    tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                }
                return content == null ? "" : String.valueOf(content);
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
                    return line;
                }
                List<Map<String, Object>> choices = (List<Map<String, Object>>) (List<?>) m.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> first = choices.get(0);
                    Map<String, Object> delta = (Map<String, Object>) first.get("delta");
                    if (delta != null && delta.get("content") != null) {
                        return String.valueOf(delta.get("content"));
                    }
                    Map<String, Object> message = (Map<String, Object>) first.get("message");
                    if (message != null && message.get("content") != null) {
                        return String.valueOf(message.get("content"));
                    }
                }
                Map<String, Object> usage = (Map<String, Object>) m.get("usage");
                if (usage != null && usage.get("total_tokens") != null) {
                    tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                }
            }
        } catch (Exception ignore) {
            // 兜底走下方
        }
        return line;
    }

    public int tokensUsed() {
        return tokensUsed.get();
    }
}
