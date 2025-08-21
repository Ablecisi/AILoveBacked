package com.ablecisi.ailovebacked.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 * 文章点赞映射器
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 01:32
 **/
@Mapper
public interface ArticleLikeRelationMapper {
    /**
     * 根据用户ID获取用户点赞的文章ID列表
     *
     * @param userId 用户ID
     * @return 点赞的文章ID列表
     */
    @Select("SELECT article_id FROM ailove.article_like_relation WHERE user_id = #{userId}")
    List<Long> getLikedArticleIdsByUserId(Long userId);
}
