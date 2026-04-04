package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AppBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class AppBootstrapController {

    private final AppBootstrapService appBootstrapService;

    @GetMapping("/bootstrap")
    public Result<Map<String, String>> bootstrap() {
        return Result.success(appBootstrapService.buildBootstrap());
    }
}
