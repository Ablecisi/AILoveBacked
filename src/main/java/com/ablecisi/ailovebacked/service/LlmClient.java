package com.ablecisi.ailovebacked.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * LlmClient
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:37
 **/
@Service
public class LlmClient {

    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final RestTemplate rest;
    private final OkHttpClient ok; // 流式

    private final AtomicInteger tokensUsed = new AtomicInteger(0);

    public LlmClient(
            @Value("${llm.apiKey}") String apiKey,
            @Value("${llm.endpoint}") String baseUrl,
            @Value("${llm.provider}") String model) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
        this.rest = new RestTemplate();
        this.ok = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(0)) // 流式不超时
                .build();
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
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(apiKey);

        try {
            ResponseEntity<Map> resp = rest.postForEntity(baseUrl, new HttpEntity<>(body, h), Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                Map result = resp.getBody();
                // choices[0].message.content
                List choices = (List) result.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map first = (Map) choices.get(0);
                    Map message = (Map) first.get("message");
                    Object content = message == null ? null : message.get("content");
                    // usage.total_tokens
                    Map usage = (Map) result.get("usage");
                    if (usage != null && usage.get("total_tokens") != null) {
                        tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                    }
                    return content == null ? "" : String.valueOf(content);
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return "抱歉，我暂时没想好该怎么回答。";
    }

    /**
     * 流式生成：逐段回调片段；返回最终拼接文本
     */
    public String generateStream(String prompt, Consumer<String> onDelta) throws IOException {
        String json = new ObjectMapper().writeValueAsString(Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "你是一个友好的AI助手"),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.6,
                "stream", true
        ));
        Request req = new Request.Builder()
                .url(baseUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json))
                .build();

        StringBuilder sb = new StringBuilder();
        try (Response resp = ok.newCall(req).execute()) {
            if (!resp.isSuccessful() || resp.body() == null) throw new IOException("HTTP " + resp.code());
            try (BufferedReader rd = new BufferedReader(resp.body().charStream())) {
                String line;
                while ((line = rd.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    // 兼容 "data: {...}" 或 纯 JSON 行 或 纯文本
                    String piece = parseDelta(line);
                    if (!piece.isEmpty()) {
                        sb.append(piece);
                        if (onDelta != null) onDelta.accept(piece);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String parseDelta(String line) {
        try {
            String s = line.startsWith("data:") ? line.substring(5).trim() : line;
            if (s.equals("[DONE]")) return "";
            if (s.startsWith("{")) {
                Map<String, Object> m = new ObjectMapper().readValue(s, Map.class);
                // Zhipu 流式字段兼容：choices[0].delta.content 或 choices[0].message.content
                List choices = (List) m.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map first = (Map) choices.get(0);
                    Map delta = (Map) first.get("delta");
                    if (delta != null && delta.get("content") != null) {
                        return String.valueOf(delta.get("content"));
                    }
                    Map message = (Map) first.get("message");
                    if (message != null && message.get("content") != null) {
                        return String.valueOf(message.get("content"));
                    }
                }
                // usage.total_tokens（可能在最后一帧）
                Map usage = (Map) m.get("usage");
                if (usage != null && usage.get("total_tokens") != null) {
                    tokensUsed.addAndGet(((Number) usage.get("total_tokens")).intValue());
                }
            }
        } catch (Exception ignore) {
        }
        return line; // 兜底：当普通文本片段
    }

    public int tokensUsed() {
        return tokensUsed.get();
    }
}

