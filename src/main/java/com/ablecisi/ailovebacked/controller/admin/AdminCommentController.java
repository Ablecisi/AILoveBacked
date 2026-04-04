package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminCommentListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminCommentManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentManageService adminCommentManageService;

    @GetMapping
    public Result<PageResult<AdminCommentListVO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long articleId,
            @RequestParam(required = false) Long postId) {
        return Result.success(adminCommentManageService.page(page, size, keyword, articleId, postId));
    }

    @GetMapping("/{id}")
    public Result<Comment> get(@PathVariable long id) {
        return Result.success(adminCommentManageService.getEntity(id));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable long id, @RequestBody Comment body) {
        adminCommentManageService.updateEntity(id, body);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> softDelete(@PathVariable long id) {
        adminCommentManageService.softDelete(id);
        return Result.success();
    }

    @DeleteMapping("/{id}/purge")
    public Result<Void> purge(@PathVariable long id) {
        adminCommentManageService.deleteHard(id);
        return Result.success();
    }
}
