package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.ArticleCollectAdminMapper;
import com.ablecisi.ailovebacked.mapper.ArticleLikeAdminMapper;
import com.ablecisi.ailovebacked.mapper.ArticleMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;
import com.ablecisi.ailovebacked.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 23:00
 **/
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final ArticleLikeAdminMapper articleLikeAdminMapper;
    private final ArticleCollectAdminMapper articleCollectAdminMapper;

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper,
                              UserMapper userMapper,
                              ArticleLikeAdminMapper articleLikeAdminMapper,
                              ArticleCollectAdminMapper articleCollectAdminMapper) {
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.articleLikeAdminMapper = articleLikeAdminMapper;
        this.articleCollectAdminMapper = articleCollectAdminMapper;
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章详情
     */
    @Override
    public ArticleVO getArticleById(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BaseException("文章未找到");
        }
        return convertToArticleVO(article);
    }

    /**
     * 根据文章ID获取相关文章列表
     *
     * @param articleId 文章ID
     * @return 相关文章列表
     */
    @Override
    public List<ArticleVO> getRelatedArticles(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return List.of();
        }

        List<String> tags = article.getTags();
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        // 查询有相同标签的文章，排除当前文章
        System.out.println(tags);
        List<Article> relatedArticles = articleMapper.selectRelatedArticlesByTags(articleId, tags);

        // 转换为 ArticleVO
        return relatedArticles.stream()
                .map(this::convertToArticleVO)
                .limit(5) // 限制返回5篇相关文章
                .toList();
    }

    /**
     * 获取推荐文章
     *
     * @param tags 用户感兴趣的标签列表
     * @return 推荐文章
     */
    @Override
    public ArticleVO getFeaturedArticle(List<String> tags) {
        Article featuredArticle = articleMapper.selectRelatedArticlesByTags(-1L, tags).stream()
                .findFirst()
                .orElse(null);
        // 如果没有找到相关标签的文章，则返回热门文章中的第一篇
        if (featuredArticle == null) {
            featuredArticle = articleMapper.selectHotArticles().stream().findFirst().orElse(null);
        }
        if (featuredArticle == null) {
            throw new BaseException("没有可推荐的文章");
        }
        return convertToArticleVO(featuredArticle);
    }

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    @Override
    public List<ArticleVO> getHotArticles() {
        // 热度计算： 点赞数 * 5 + 评论数 * 3 + 浏览量 * 1
        // 直接从数据库中获取前10篇热门文章
        List<Article> hotArticles = articleMapper.selectHotArticles();
        return hotArticles.stream()
                .map(this::convertToArticleVO)
                .limit(10) // 限制返回10篇热门文章
                .toList();
    }

    @Override
    public void recordArticleView(Long articleId) {
        Article a = articleMapper.selectById(articleId);
        if (a == null) {
            return;
        }
        articleMapper.incrementViewCount(articleId);
    }

    /**
     * 转换 Article 实体为 ArticleVO
     *
     * @param article 文章实体
     * @return 文章视图对象
     */
    private ArticleVO convertToArticleVO(Article article) {
        if (article.getTags() == null) {
            article.setTags(List.of());
        }
        User author = article.getUserId() != null ? userMapper.getUserById(article.getUserId()) : null;
        String authorName = author != null && author.getName() != null && !author.getName().isBlank()
                ? author.getName()
                : (author != null ? author.getUsername() : "作者");
        String authorAvatar = author != null && author.getAvatarUrl() != null ? author.getAvatarUrl() : "";

        Long uid = BaseContext.getCurrentId();
        boolean liked = uid != null && articleLikeAdminMapper.selectByUserAndArticle(uid, article.getId()) != null;
        boolean bookmarked = uid != null && articleCollectAdminMapper.selectByUserAndArticle(uid, article.getId()) != null;

        return ArticleVO.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .coverImageUrl(article.getCoverImageUrl())
                .authorId(String.valueOf(article.getUserId()))
                .authorName(authorName)
                .authorAvatarUrl(authorAvatar)
                .publishTime(article.getCreateTime())
                .viewCount(article.getViewCount())
                .likeCount(article.getLikeCount())
                .commentCount(article.getCommentCount())
                .isLiked(liked)
                .isBookmarked(bookmarked)
                .tags(article.getTags())
                .comments(List.of())
                .build();
    }
}
