package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminPostManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostManageService adminPostManageService;

    @GetMapping
    public Result<PageResult<Post>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(adminPostManageService.page(page, size));
    }

    @GetMapping("/{id}")
    public Result<Post> get(@PathVariable long id) {
        return Result.success(adminPostManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody Post body) {
        return Result.success(Map.of("id", adminPostManageService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody Post body) {
        adminPostManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminPostManageService.delete(id);
        return Result.success();
    }
}
