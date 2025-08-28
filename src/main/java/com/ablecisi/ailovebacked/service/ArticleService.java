package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 22:59
 **/
public interface ArticleService {
    /**
     * 根据文章ID获取文章详情
     *
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleVO getArticleById(Long articleId);

    /**
     * 根据文章ID获取相关文章列表
     *
     * @param articleId 文章ID
     * @return 相关文章列表
     */
    List<ArticleVO> getRelatedArticles(Long articleId);

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    List<ArticleVO> getHotArticles();

    /**
     * 获取推荐文章
     *
     * @param tags 用户感兴趣的标签列表
     * @return 推荐文章
     */
    ArticleVO getFeaturedArticle(List<String> tags);
}
