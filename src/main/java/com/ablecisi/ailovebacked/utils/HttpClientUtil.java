package com.ablecisi.ailovebacked.utils;

import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于 JDK {@link java.net.http.HttpClient} 的同步 HTTP 工具。
 * <ul>
 *   <li>请求体 / 响应体可与 JSON 互转（配合 {@link JsonUtil}）</li>
 *   <li>可将 JSON 对象字符串解析为查询参数（仅一层键值，值会 {@code String.valueOf}）</li>
 * </ul>
 */
public final class HttpClientUtil {

    /**
     * 默认连接超时
     * 15 秒
     */
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(15);
    /**
     * 默认请求超时
     * 60 秒
     */
    private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(60);

    /**
     * 流式接口（如 LLM SSE）单次请求可读上限，避免与默认 60s 冲突
     */
    private static final Duration STREAM_REQUEST_TIMEOUT = Duration.ofHours(6);

    /**
     * HTTP 客户端
     */
    private static final java.net.http.HttpClient CLIENT = java.net.http.HttpClient.newBuilder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT)
            .followRedirects(java.net.http.HttpClient.Redirect.NORMAL)
            .version(java.net.http.HttpClient.Version.HTTP_2)
            .build();

    private HttpClientUtil() {
    }

    /**
     * HTTP 调用结果
     *
     * @param statusCode 状态码
     * @param body       响应正文（可能为空串）
     */
    public record HttpResult(int statusCode, String body) {
        public boolean is2xx() {
            return statusCode >= 200 && statusCode < 300;
        }

        public <T> T parseBody(Class<T> clazz) {
            if (body == null || body.isBlank()) {
                return null;
            }
            return JsonUtil.fromJson(body, clazz);
        }
    }

    // ——— GET ———

    public static HttpResult get(String url) {
        return get(url, Map.of(), Map.of());
    }

    public static HttpResult get(String url, Map<String, String> headers) {
        return get(url, Map.of(), headers);
    }

    /**
     * GET，查询参数为扁平 Map（值会做 URL 编码）
     */
    public static HttpResult get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        String full = appendQuery(url, queryParams);
        return execute("GET", full, null, headers);
    }

    /**
     * GET，查询参数由 JSON 对象字符串解析，例如 {@code {"page":"1","size":"10"}}
     */
    public static HttpResult getWithJsonParams(String url, String jsonParams, Map<String, String> headers) {
        return get(url, jsonObjectToFlatMap(jsonParams), headers);
    }

    // ——— POST JSON ———

    public static HttpResult postJson(String url, Object body) {
        return postJson(url, body, Map.of());
    }

    /**
     * POST，body 为任意对象，序列化为 JSON（{@link JsonUtil#toJson}）
     */
    public static HttpResult postJson(String url, Object body, Map<String, String> headers) {
        String json = body instanceof String s ? s : JsonUtil.toJson(body);
        return execute("POST", url, json, withJsonContentType(headers));
    }

    /**
     * POST，body 已是 JSON 字符串
     */
    public static HttpResult postJsonRaw(String url, String jsonBody, Map<String, String> headers) {
        return execute("POST", url, jsonBody, withJsonContentType(headers));
    }

    /**
     * POST JSON，返回响应体字节流（适用于 SSE/分块读取）。调用方必须在结束后关闭流。
     * 非 2xx 时会读完错误正文后抛出 {@link UncheckedIOException}。
     */
    public static InputStream postJsonStream(String url, Object body, Map<String, String> headers) {
        Objects.requireNonNull(url, "url");
        String json = body instanceof String s ? s : JsonUtil.toJson(body);
        Map<String, String> h = withJsonContentType(headers == null ? new LinkedHashMap<>() : new LinkedHashMap<>(headers));

        try {
            java.net.http.HttpRequest.Builder b = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(STREAM_REQUEST_TIMEOUT)
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8));
            h.forEach(b::header);

            java.net.http.HttpResponse<InputStream> resp = CLIENT.send(
                    b.build(),
                    java.net.http.HttpResponse.BodyHandlers.ofInputStream());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                return resp.body();
            }
            try (InputStream err = resp.body()) {
                if (err != null) {
                    err.readAllBytes();
                }
            }
            throw new UncheckedIOException(new IOException("HTTP " + resp.statusCode()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpClientException("请求被中断: " + url, e);
        } catch (IOException e) {
            throw new UncheckedIOException("HTTP 流式请求失败: " + url, e);
        }
    }

    // ——— PUT / PATCH / DELETE ———

    public static HttpResult putJson(String url, Object body, Map<String, String> headers) {
        String json = body instanceof String s ? s : JsonUtil.toJson(body);
        return execute("PUT", url, json, withJsonContentType(headers));
    }

    public static HttpResult patchJson(String url, Object body, Map<String, String> headers) {
        String json = body instanceof String s ? s : JsonUtil.toJson(body);
        return execute("PATCH", url, json, withJsonContentType(headers));
    }

    public static HttpResult delete(String url) {
        return delete(url, Map.of());
    }

    public static HttpResult delete(String url, Map<String, String> headers) {
        return execute("DELETE", url, null, headers);
    }

    /**
     * 通用执行：method 为 GET/POST/PUT/PATCH/DELETE 等
     *
     * @param method  HTTP 方法
     * @param url     完整 URL（已带 query 若需要）
     * @param body    非 GET/DELETE 时可传 JSON 字符串；无正文传 null
     * @param headers 额外请求头（大小写不敏感，会按传入原样设置）
     */
    public static HttpResult execute(String method, String url, String body, Map<String, String> headers) {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(url, "url");
        Map<String, String> h = headers == null ? Map.of() : headers;

        try {
            java.net.http.HttpRequest.Builder b = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(DEFAULT_REQUEST_TIMEOUT);

            String upper = method.trim().toUpperCase();
            if ("GET".equals(upper) || "DELETE".equals(upper)) {
                b.method(upper, java.net.http.HttpRequest.BodyPublishers.noBody());
            } else {
                String payload = body == null ? "" : body;
                b.method(upper, java.net.http.HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8));
            }
            h.forEach(b::header);

            java.net.http.HttpResponse<String> resp = CLIENT.send(
                    b.build(),
                    java.net.http.HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return new HttpResult(resp.statusCode(), resp.body() == null ? "" : resp.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpClientException("请求被中断: " + url, e);
        } catch (IOException e) {
            throw new UncheckedIOException("HTTP 请求失败: " + url, e);
        }
    }

    /**
     * 将 JSON 对象字符串转为单层 {@code Map<String,String>}（用于 query / 表单场景）
     */
    public static Map<String, String> jsonObjectToFlatMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        JSONObject obj = JSONObject.parseObject(json);
        Map<String, String> out = new LinkedHashMap<>();
        for (String key : obj.keySet()) {
            Object v = obj.get(key);
            out.put(key, v == null ? "" : String.valueOf(v));
        }
        return out;
    }

    /**
     * 添加 query 参数
     *
     * @param url         原始 URL
     * @param queryParams query 参数
     * @return 添加 query 参数后的 URL
     */
    public static String appendQuery(String url, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return url;
        }
        String q = queryParams.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
                .collect(Collectors.joining("&"));
        if (url.contains("?")) {
            return url.endsWith("?") || url.endsWith("&") ? url + q : url + "&" + q;
        }
        return url + "?" + q;
    }

    /**
     * URL 编码
     *
     * @param s 原始字符串
     * @return 编码后的字符串
     */
    private static String encode(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }

    /**
     * 添加 JSON Content-Type
     *
     * @param headers 原始请求头
     * @return 添加 Content-Type 后的请求头
     */
    private static Map<String, String> withJsonContentType(Map<String, String> headers) {
        boolean hasCt = headers != null && headers.keySet().stream()
                .anyMatch(k -> k != null && k.equalsIgnoreCase("Content-Type"));
        if (hasCt) {
            return headers;
        }
        Map<String, String> m = new LinkedHashMap<>();
        if (headers != null) {
            m.putAll(headers);
        }
        m.putIfAbsent("Content-Type", "application/json; charset=UTF-8");
        return m;
    }

    /**
     * 网络或中断等非 HTTP 状态类异常
     */
    public static class HttpClientException extends RuntimeException {
        public HttpClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
