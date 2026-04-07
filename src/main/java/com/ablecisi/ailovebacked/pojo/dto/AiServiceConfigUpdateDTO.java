package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理端更新 AI 服务配置。{@code apiKey} 为空串表示不修改已保存的密钥。
 */
@Data
public class AiServiceConfigUpdateDTO {

    @NotBlank(message = "模型标识不能为空")
    private String provider;

    @NotBlank(message = "LLM 接口 URL 不能为空")
    private String endpoint;

    /**
     * 新 API Key；若为 null 或空字符串，则不更新库中的 llm.apiKey。
     */
    private String apiKey;

    private boolean includeReasoningContent;

    /**
     * 情绪识别服务地址；可为空表示不调用远程服务（将走本地降级）。
     */
    private String bertUrl;
}
