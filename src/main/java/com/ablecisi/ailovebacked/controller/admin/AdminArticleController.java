package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminArticleListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminArticleManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final AdminArticleManageService adminArticleManageService;

    @GetMapping
    public Result<PageResult<AdminArticleListVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(adminArticleManageService.page(page, size, keyword));
    }

    @GetMapping("/{id}")
    public Result<Article> get(@PathVariable long id) {
        return Result.success(adminArticleManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody Article body) {
        return Result.success(Map.of("id", adminArticleManageService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody Article body) {
        adminArticleManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminArticleManageService.delete(id);
        return Result.success();
    }
}
