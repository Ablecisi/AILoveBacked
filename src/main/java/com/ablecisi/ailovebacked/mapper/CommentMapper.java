package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 01:37
 **/
@Mapper
public interface CommentMapper {
    /**
     * 根据文章ID获取评论列表
     *
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<Comment> getCommentsByArticleId(Long articleId);

    /**
     * 根据评论ID获取回复列表
     *
     * @param commentIds 评论ID列表
     * @return 回复列表
     */
    List<Comment> getRepliesByCommentIds(List<Long> commentIds);
}
