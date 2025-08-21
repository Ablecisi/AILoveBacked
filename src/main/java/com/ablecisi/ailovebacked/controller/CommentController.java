package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AILoveBacked
 * <br>
 * com.ablecisi.ailovebacked.controller <br>
 * 评论控制器 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 14:25
 **/
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    /**
     * 根据文章ID获取评论列表
     *
     * @param articleId 文章ID
     * @return 评论列表
     */
    @GetMapping()
    public Result<List<CommentVO>> getCommentsByArticleId(@RequestParam String articleId) {
        log.info("获取文章评论，文章ID：{}", articleId);
        List<CommentVO> comments = commentService.getCommentsByArticleId(Long.parseLong(articleId));
        if (comments.isEmpty()) {
            log.warn("没有找到文章ID为 {} 的评论", articleId);
            return Result.error("没有找到评论");
        }
        log.info("成功获取文章ID为 {} 的评论 {}", articleId, comments);
        return Result.success(comments);
    }
}
