package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.Conversation;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminConversationListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminConversationWriteService;
import com.ablecisi.ailovebacked.service.AdminDialogManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/conversations")
@RequiredArgsConstructor
public class AdminConversationController {

    private final AdminDialogManageService adminDialogManageService;
    private final AdminConversationWriteService adminConversationWriteService;

    @GetMapping
    public Result<PageResult<AdminConversationListVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long userId) {
        return Result.success(adminDialogManageService.pageConversations(page, size, keyword, userId));
    }

    @GetMapping("/{id}")
    public Result<Conversation> get(@PathVariable long id) {
        return Result.success(adminConversationWriteService.get(id));
    }

    @PostMapping
    public Result<Map<String, Long>> create(@RequestBody Conversation body) {
        return Result.success(Map.of("id", adminConversationWriteService.create(body)));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody Conversation body) {
        adminConversationWriteService.update(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable long id) {
        adminConversationWriteService.delete(id);
        return Result.success();
    }
}
