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
 * 2025/6/15
 * 星期日
 * 19:30
 **/
@Mapper
public interface ArticleMapper {
    /**
     * 获取用户精选文章
     *
     * @param userId 用户ID
     * @return 精选文章视图对象
     */
    Article getFeaturedArticles(String userId);

    /**
     * 根据文章ID获取文章详情
     *
     * @param id 文章ID
     * @return 文章视图对象
     */
    Article getArticleById(Long id);

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章ID
     */
    void updateViewCount(Long articleId);

    /**
     * 更新文章点赞数
     *
     * @param articleId 文章ID
     */
    void updateLikeCount(Long articleId);

    /**
     * 获取热门文章列表
     *
     * @return 热门文章视图对象列表
     */
    List<Article> getHotArticles();
}
