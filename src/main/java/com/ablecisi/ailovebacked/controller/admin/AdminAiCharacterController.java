package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminAiCharacterWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminAiCharacterManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/ai-characters")
@RequiredArgsConstructor
public class AdminAiCharacterController {

    private final AdminAiCharacterManageService adminAiCharacterManageService;

    @GetMapping
    public Result<PageResult<AiCharacterVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer online) {
        return Result.success(adminAiCharacterManageService.page(page, size, keyword, typeId, status, online));
    }

    @GetMapping("/{id}")
    public Result<AiCharacterVO> get(@PathVariable long id) {
        return Result.success(adminAiCharacterManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody AdminAiCharacterWriteDTO body) {
        long newId = adminAiCharacterManageService.create(body);
        return Result.success(Map.of("id", newId));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody AdminAiCharacterWriteDTO body) {
        adminAiCharacterManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminAiCharacterManageService.delete(id);
        return Result.success();
    }
}
