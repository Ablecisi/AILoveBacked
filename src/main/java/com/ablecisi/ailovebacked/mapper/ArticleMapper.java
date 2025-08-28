package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 22:59
 **/
@Mapper
public interface ArticleMapper {

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章详情
     */
    Article selectById(Long articleId);

    /**
     * 根据文章ID获取相关文章列表
     *
     * @param articleId 文章ID
     * @param tags      文章标签
     * @return 相关文章列表
     */
    List<Article> selectRelatedArticlesByTags(Long articleId, List<String> tags);

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    List<Article> selectHotArticles();

}
