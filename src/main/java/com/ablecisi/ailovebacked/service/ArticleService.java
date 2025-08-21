package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:31
 **/
public interface ArticleService {

    /**
     * 获取用户精选文章
     *
     * @param userId 用户ID
     * @return 精选文章视图对象
     */
    ArticleVO getFeaturedArticles(String userId);

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章视图对象
     */
    ArticleVO getArticleById(Long articleId);

    /**
     * 获取热门文章列表
     *
     * @return 热门文章视图对象列表
     */
    List<ArticleVO> getHotArticles();
}
