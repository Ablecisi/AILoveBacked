package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.dto.AiServiceConfigUpdateDTO;
import com.ablecisi.ailovebacked.pojo.vo.AiServiceConfigVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AiRuntimeConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端：AI 大模型与情绪识别接口配置（写入 app_config，运行时由 {@link AiRuntimeConfigService} 消费）。
 */
@RestController
@RequestMapping("/admin/api/v1/ai-service-config")
@RequiredArgsConstructor
public class AdminAiServiceConfigController {

    private final AiRuntimeConfigService aiRuntimeConfigService;
    private final AppConfigMapper appConfigMapper;

    /**
     * 获取当前生效配置（API Key 仅返回是否已配置，不返回明文）。
     */
    @GetMapping
    public Result<AiServiceConfigVO> get() {
        aiRuntimeConfigService.refresh();
        AiServiceConfigVO vo = AiServiceConfigVO.builder()
                .provider(aiRuntimeConfigService.getLlmProvider())
                .endpoint(aiRuntimeConfigService.getLlmEndpoint())
                .apiKeyConfigured(aiRuntimeConfigService.isLlmApiKeyConfigured())
                .includeReasoningContent(aiRuntimeConfigService.isIncludeReasoningContent())
                .bertUrl(aiRuntimeConfigService.getBertUrl())
                .build();
        return Result.success(vo);
    }

    /**
     * 保存配置项到 app_config 并刷新运行时缓存。
     */
    @PutMapping
    public Result<Void> update(@RequestBody @Valid AiServiceConfigUpdateDTO dto) {
        appConfigMapper.upsert("llm.provider", dto.getProvider().trim());
        appConfigMapper.upsert("llm.endpoint", dto.getEndpoint().trim());
        appConfigMapper.upsert("llm.include-reasoning-content", dto.isIncludeReasoningContent() ? "true" : "false");
        String bert = dto.getBertUrl() != null ? dto.getBertUrl().trim() : "";
        appConfigMapper.upsert("ml.bert.url", bert);
        if (dto.getApiKey() != null && !dto.getApiKey().isBlank()) {
            appConfigMapper.upsert("llm.apiKey", dto.getApiKey().trim());
        }
        aiRuntimeConfigService.refresh();
        return Result.success();
    }
}
