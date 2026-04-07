package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.entity.AppConfigRow;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 从 {@code app_config} 读取 LLM / BERT 相关配置，未配置时回退到 {@code application.yml}。
 * 管理端保存后需调用 {@link #refresh()} 使当前进程立即生效。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiRuntimeConfigService {

    private final AppConfigMapper appConfigMapper;

    @Value("${llm.apiKey:}")
    private String defaultLlmApiKey;
    @Value("${llm.endpoint:}")
    private String defaultLlmEndpoint;
    @Value("${llm.provider:}")
    private String defaultLlmProvider;
    @Value("${llm.include-reasoning-content:false}")
    private boolean defaultIncludeReasoning;
    @Value("${ml.bert.url:}")
    private String defaultBertUrl;

    private volatile Map<String, String> snapshot = Map.of();

    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * 从数据库重新加载全部 app_config 到内存快照（轻量表，全量扫描即可）。
     */
    public synchronized void refresh() {
        Map<String, String> m = new HashMap<>();
        for (AppConfigRow row : appConfigMapper.selectAll()) {
            if (row.getConfigKey() != null && row.getConfigValue() != null) {
                m.put(row.getConfigKey(), row.getConfigValue());
            }
        }
        snapshot = Collections.unmodifiableMap(m);
        log.debug("AiRuntimeConfig refreshed, keys={}", snapshot.size());
    }

    private String cfg(String key) {
        return snapshot.get(key);
    }

    private static String firstNonBlank(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred.trim();
        }
        return fallback != null ? fallback.trim() : "";
    }

    /**
     * @return LLM API Key（可能为空，调用方需处理）
     */
    public String getLlmApiKey() {
        return firstNonBlank(cfg("llm.apiKey"), defaultLlmApiKey);
    }

    /**
     * @return 兼容 OpenAI 的 Chat Completions 完整 URL
     */
    public String getLlmEndpoint() {
        return firstNonBlank(cfg("llm.endpoint"), defaultLlmEndpoint);
    }

    /**
     * @return 模型名（对应请求体 model 字段）
     */
    public String getLlmProvider() {
        return firstNonBlank(cfg("llm.provider"), defaultLlmProvider);
    }

    public boolean isIncludeReasoningContent() {
        String v = cfg("llm.include-reasoning-content");
        if (v != null && !v.isBlank()) {
            return "true".equalsIgnoreCase(v.trim()) || "1".equals(v.trim());
        }
        return defaultIncludeReasoning;
    }

    /**
     * @return BERT/情绪识别 HTTP 接口地址
     */
    public String getBertUrl() {
        return firstNonBlank(cfg("ml.bert.url"), defaultBertUrl);
    }

    public boolean isLlmApiKeyConfigured() {
        return !getLlmApiKey().isBlank();
    }
}
