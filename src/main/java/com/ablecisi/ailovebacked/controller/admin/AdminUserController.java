package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminUserWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserDetailVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminUserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserManageService adminUserManageService;

    @GetMapping
    public Result<PageResult<AdminUserListVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(adminUserManageService.page(page, size, keyword));
    }

    @GetMapping("/{id}")
    public Result<AdminUserDetailVO> get(@PathVariable long id) {
        return Result.success(adminUserManageService.getDetail(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody AdminUserWriteDTO body) {
        long newId = adminUserManageService.create(body);
        return Result.success(Map.of("id", newId));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody AdminUserWriteDTO body) {
        adminUserManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminUserManageService.delete(id);
        return Result.success();
    }
}
