package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminOpsAccountWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminOpsAccountVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminOpsUserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/ops-accounts")
@RequiredArgsConstructor
public class AdminOpsAccountController {

    private final AdminOpsUserManageService adminOpsUserManageService;

    @GetMapping
    public Result<List<AdminOpsAccountVO>> list() {
        return Result.success(adminOpsUserManageService.listAll());
    }

    @GetMapping("/{id}")
    public Result<AdminOpsAccountVO> get(@PathVariable long id) {
        return Result.success(adminOpsUserManageService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody AdminOpsAccountWriteDTO body) {
        return Result.success(Map.of("id", adminOpsUserManageService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody AdminOpsAccountWriteDTO body) {
        adminOpsUserManageService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminOpsUserManageService.delete(id);
        return Result.success();
    }
}
