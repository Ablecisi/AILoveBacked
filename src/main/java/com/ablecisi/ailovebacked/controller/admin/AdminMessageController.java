package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminMessageListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminDialogManageService;
import com.ablecisi.ailovebacked.service.AdminMessageWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/messages")
@RequiredArgsConstructor
public class AdminMessageController {

    private final AdminDialogManageService adminDialogManageService;
    private final AdminMessageWriteService adminMessageWriteService;

    @GetMapping
    public Result<PageResult<AdminMessageListVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long conversationId) {
        return Result.success(adminDialogManageService.pageMessages(page, size, keyword, conversationId));
    }

    @GetMapping("/{id}")
    public Result<Message> get(@PathVariable long id) {
        return Result.success(adminMessageWriteService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody Message body) {
        return Result.success(Map.of("id", adminMessageWriteService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody Message body) {
        adminMessageWriteService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminMessageWriteService.delete(id);
        return Result.success();
    }
}
