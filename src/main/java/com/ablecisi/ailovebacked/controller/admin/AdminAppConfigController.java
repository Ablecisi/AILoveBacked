package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.dto.AppConfigValueDTO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AiRuntimeConfigService;
import com.ablecisi.ailovebacked.service.AppBootstrapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/app-config")
@RequiredArgsConstructor
public class AdminAppConfigController {

    private final AppBootstrapService appBootstrapService;
    private final AppConfigMapper appConfigMapper;
    private final AiRuntimeConfigService aiRuntimeConfigService;

    @GetMapping
    public Result<Map<String, String>> list() {
        return Result.success(appBootstrapService.buildBootstrapForAdmin());
    }

    @PutMapping("/{key:.+}")
    public Result<Void> upsert(@PathVariable("key") String key, @RequestBody @Valid AppConfigValueDTO body) {
        appConfigMapper.upsert(key, body.getValue() != null ? body.getValue() : "");
        if (key != null && (key.startsWith("llm.") || key.startsWith("ml.bert"))) {
            aiRuntimeConfigService.refresh();
        }
        return Result.<Void>success();
    }
}
