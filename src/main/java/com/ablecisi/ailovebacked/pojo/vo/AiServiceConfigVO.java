package com.ablecisi.ailovebacked.pojo.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 管理端展示的 AI 服务配置（不包含 API Key 明文）。
 */
@Data
@Builder
public class AiServiceConfigVO {
    /**
     * 对应请求体 model 字段
     */
    private String provider;
    /**
     * Chat Completions 等接口完整 URL
     */
    private String endpoint;
    /**
     * 是否已在库或环境中配置过非空 API Key
     */
    private boolean apiKeyConfigured;
    /**
     * 是否在正文为空时使用 reasoning_content
     */
    private boolean includeReasoningContent;
    /**
     * 情绪识别服务 URL
     */
    private String bertUrl;
}
