package com.ablecisi.ailovebacked.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 02:06
 **/
@Mapper
public interface CommentLikeRelationMapper {
    /**
     * 根据用户ID获取用户点赞的评论ID列表
     *
     * @param id 用户ID
     * @return 点赞的评论ID列表
     */
    @Select("SELECT comment_id FROM ailove.comment_like_relation WHERE user_id = #{id}")
    List<Long> getLikedCommentIdsByUserId(Long id);

    /**
     * 根据评论ID获取点赞数
     *
     * @param commentId 评论ID
     * @return 点赞数
     */
    @Select("SELECT COUNT(*) FROM ailove.comment_like_relation WHERE comment_id = #{commentId}")
    Integer getLikeCountByCommentId(Long commentId);
}
