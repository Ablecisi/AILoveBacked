package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.TypeItem;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminTypeManageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/types")
@RequiredArgsConstructor
public class AdminTypeController {

    private final AdminTypeManageService adminTypeManageService;

    @GetMapping
    public Result<List<TypeItem>> list() {
        return Result.success(adminTypeManageService.listAll());
    }

    @GetMapping("/{id}")
    public Result<TypeItem> get(@PathVariable long id) {
        return Result.success(adminTypeManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody TypeWriteBody body) {
        return Result.success(Map.of("id", adminTypeManageService.create(body.getName(), body.getPromptStyle())));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody TypeWriteBody body) {
        adminTypeManageService.update(id, body.getName(), body.getPromptStyle());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminTypeManageService.delete(id);
        return Result.success();
    }

    @Data
    public static class TypeWriteBody {
        private String name;
        /**
         * 对话语气/风格，可选
         */
        private String promptStyle;
    }
}
