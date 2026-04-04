package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminUserProfileManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1/user-profiles")
@RequiredArgsConstructor
public class AdminUserProfileController {

    private final AdminUserProfileManageService adminUserProfileManageService;

    @GetMapping
    public Result<PageResult<UserProfile>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(adminUserProfileManageService.page(page, size));
    }

    @GetMapping("/{userId}")
    public Result<UserProfile> get(@PathVariable long userId) {
        return Result.success(adminUserProfileManageService.get(userId));
    }

    @PostMapping
    public Result<Void> save(@RequestBody UserProfile body) {
        adminUserProfileManageService.save(body);
        return Result.success();
    }

    @PutMapping("/{userId}")
    public Result<Void> saveByPath(@PathVariable long userId, @RequestBody UserProfile body) {
        body.setUserId(userId);
        adminUserProfileManageService.save(body);
        return Result.success();
    }

    @DeleteMapping("/{userId}")
    public Result<Void> delete(@PathVariable long userId) {
        adminUserProfileManageService.delete(userId);
        return Result.success();
    }
}
