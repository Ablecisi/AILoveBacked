package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.FollowRelation;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminFollowManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/follow-relations")
@RequiredArgsConstructor
public class AdminFollowController {

    private final AdminFollowManageService adminFollowManageService;

    @GetMapping
    public Result<PageResult<FollowRelation>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(adminFollowManageService.page(page, size));
    }

    @GetMapping("/{id}")
    public Result<FollowRelation> get(@PathVariable long id) {
        return Result.success(adminFollowManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody FollowRelation body) {
        return Result.success(Map.of("id", adminFollowManageService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody FollowRelation body) {
        adminFollowManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminFollowManageService.delete(id);
        return Result.success();
    }
}
