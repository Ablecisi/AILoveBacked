package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.CommentVO;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 * 评论服务接口 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 14:28
 **/
public interface CommentService {
    /**
     * 根据文章ID获取评论列表
     *
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<CommentVO> getCommentsByArticleId(Long articleId);
}
