package com.ablecisi.ailovebacked.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 * 文章收藏映射器
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 01:32
 **/
@Mapper
public interface ArticleCollectionRelationMapper {

    @Select("SELECT article_id FROM ailove.article_collect_relation WHERE user_id = #{userId}")
    List<Long> getCollectedArticleIdsByUserId(Long userId);
}
