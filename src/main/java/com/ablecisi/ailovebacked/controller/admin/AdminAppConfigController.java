package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.dto.AppConfigValueDTO;
import com.ablecisi.ailovebacked.result.Result;
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

    @GetMapping
    public Result<Map<String, String>> list() {
        return Result.success(appBootstrapService.buildBootstrap());
    }

    @PutMapping("/{key:.+}")
    public Result<Void> upsert(@PathVariable("key") String key, @RequestBody @Valid AppConfigValueDTO body) {
        appConfigMapper.upsert(key, body.getValue() != null ? body.getValue() : "");
        return Result.<Void>success();
    }
}
