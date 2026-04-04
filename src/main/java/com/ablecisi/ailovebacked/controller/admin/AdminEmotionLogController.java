package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.EmotionLog;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminEmotionLogManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/emotion-logs")
@RequiredArgsConstructor
public class AdminEmotionLogController {

    private final AdminEmotionLogManageService adminEmotionLogManageService;

    @GetMapping
    public Result<PageResult<EmotionLog>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(adminEmotionLogManageService.page(page, size));
    }

    @GetMapping("/{id}")
    public Result<EmotionLog> get(@PathVariable long id) {
        return Result.success(adminEmotionLogManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody EmotionLog body) {
        return Result.success(Map.of("id", adminEmotionLogManageService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody EmotionLog body) {
        adminEmotionLogManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminEmotionLogManageService.delete(id);
        return Result.success();
    }
}
