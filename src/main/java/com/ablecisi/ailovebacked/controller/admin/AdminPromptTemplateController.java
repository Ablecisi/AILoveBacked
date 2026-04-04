package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptTemplateDTO;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminPromptManageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/prompt-templates")
@RequiredArgsConstructor
public class AdminPromptTemplateController {

    private final AdminPromptManageService adminPromptManageService;

    @GetMapping
    public Result<List<PromptTemplate>> list() {
        return Result.success(adminPromptManageService.listAll());
    }

    @GetMapping("/{id}")
    public Result<PromptTemplate> get(@PathVariable long id) {
        return Result.success(adminPromptManageService.getById(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody @Valid AdminPromptTemplateDTO body) {
        long newId = adminPromptManageService.create(body);
        return Result.success(Map.of("id", newId));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody @Valid AdminPromptTemplateDTO body) {
        adminPromptManageService.update(id, body);
        return Result.success();
    }
}
