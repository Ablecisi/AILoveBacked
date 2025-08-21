package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.*;
import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:31
 **/
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final ArticleCollectionRelationMapper articleCollectionRelationMapper;
    private final ArticleLikeRelationMapper articleLikeRelationMapper;
    private final CommentMapper commentMapper;
    private final CommentLikeRelationMapper commentLikeRelationMapper;

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper,
                              UserMapper userMapper,
                              ArticleCollectionRelationMapper articleCollectionRelationMapper,
                              ArticleLikeRelationMapper articleLikeRelationMapper,
                              CommentMapper commentMapper,
                              CommentLikeRelationMapper commentLikeRelationMapper
    ) {
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.articleCollectionRelationMapper = articleCollectionRelationMapper;
        this.articleLikeRelationMapper = articleLikeRelationMapper;
        this.commentMapper = commentMapper;
        this.commentLikeRelationMapper = commentLikeRelationMapper;
    }

    /**
     * 获取用户精选文章
     *
     * @param userId 用户ID
     * @return 精选文章视图对象
     */
    @Override
    @Transactional
    public ArticleVO getFeaturedArticles(String userId) {
        // 调用Mapper方法获取精选文章
        Article article = articleMapper.getFeaturedArticles(userId);
        if (article == null) {
            // 如果没有找到文章，返回null
            return null;
        }
        Long id = Long.parseLong(userId);

        // 获取评论
        List<Comment> comments = commentMapper.getCommentsByArticleId(article.getId());
        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();
        // 如果没有评论，返回空列表
        List<CommentVO> commentVOs;
        if (!comments.isEmpty()) {
            User commentUser = userMapper.getUserById(article.getUserId());
            commentVOs = comments.stream()
                    .map(comment -> CommentVO.builder()
                            .id(String.valueOf(comment.getId()))
                            .content(comment.getContent())
                            .userId(String.valueOf(comment.getUserId()))
                            .userName(commentUser.getName())
                            .userAvatarUrl(commentUser.getAvatarUrl())
                            .parentId(String.valueOf(comment.getParentId()))
                            .replies(null) // 暂时不处理回复
                            .createTime(comment.getCreateTime().toEpochSecond(ZoneOffset.UTC) * 1000) // 转换为时间戳
                            .build())
                    .toList();

            // 检查每个评论是否被用户点赞
            for (CommentVO commentVO : commentVOs) {
                // 获取用户点赞的评论ID列表
                List<Long> likedComments = commentLikeRelationMapper.getLikedCommentIdsByUserId(id);
                // 检查评论是否被用户点赞
                boolean isLiked = likedComments.contains(Long.parseLong(commentVO.getId()));
                commentVO.setIsLiked(isLiked);
            }
            // 设置评论的点赞数
            for (CommentVO commentVO : commentVOs) {
                Long commentId = Long.parseLong(commentVO.getId());
                Integer likeCount = commentLikeRelationMapper.getLikeCountByCommentId(commentId);
                commentVO.setLikeCount(likeCount != null ? likeCount : 0);
            }
        } else {
            // 如果没有评论，返回空列表
            commentVOs = List.of();
        }

        // 获取用户收藏的文章ID列表
        List<Long> collectedArticles = articleCollectionRelationMapper.getCollectedArticleIdsByUserId(id);
        // 检查文章是否被用户收藏
        boolean isCollected = collectedArticles.contains(article.getId());

        // 获取用户点赞的文章ID列表
        List<Long> likedArticles = articleLikeRelationMapper.getLikedArticleIdsByUserId(id);
        // 检查文章是否被用户点赞
        boolean isLiked = likedArticles.contains(article.getId());

        // 获取文章作者信息
        User articleAuthor = userMapper.getUserById(article.getUserId());
        // 返回获取到的文章视图对象
        return ArticleVO.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .coverImageUrl(article.getCoverImageUrl())
                .authorId(String.valueOf(article.getUserId()))
                .authorName(articleAuthor.getName())
                .authorAvatarUrl(articleAuthor.getAvatarUrl())
                .likeCount(article.getLikeCount())
                .viewCount(article.getViewCount())
                .isLiked(isLiked)
                .isBookmarked(isCollected)
                .tags(article.getTags())
                .comments(commentVOs)
                .commentCount(commentVOs.size())
                .publishTime(article.getCreateTime())
                .build();
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章视图对象
     */
    @Override
    @Transactional
    public ArticleVO getArticleById(Long articleId) {
        // 查之前先更新点赞数和浏览量和评论数
        // 更新文章的浏览量
        articleMapper.updateViewCount(articleId);
        // 更新文章的点赞数
        articleMapper.updateLikeCount(articleId);

        // 调用Mapper方法获取文章
        Article article = articleMapper.getArticleById(articleId);
        if (article == null) {
            // 如果没有找到文章，返回null
            return null;
        }

        // 获取用户收藏的文章ID列表
        List<Long> collectedArticles = articleCollectionRelationMapper.getCollectedArticleIdsByUserId(article.getUserId());
        // 检查文章是否被用户收藏
        boolean isCollected = collectedArticles.contains(article.getId());

        // 获取用户点赞的文章ID列表
        List<Long> likedArticles = articleLikeRelationMapper.getLikedArticleIdsByUserId(article.getUserId());
        // 检查文章是否被用户点赞
        boolean isLiked = likedArticles.contains(article.getId());

        // 获取文章作者信息
        User articleAuthor = userMapper.getUserById(article.getUserId());

        log.info("获取到文章tag {}", article.getTags());
        if (article.getTags() == null) {
            article.setTags(List.of()); // 如果没有标签，设置为空列表
        }
        // 返回获取到的文章视图对象
        return ArticleVO.builder()
                .id(String.valueOf(article.getId()))
                .title(article.getTitle())
                .description(article.getDescription())
                .content(article.getContent())
                .coverImageUrl(article.getCoverImageUrl())
                .authorId(String.valueOf(article.getUserId()))
                .authorName(articleAuthor.getName())
                .authorAvatarUrl(articleAuthor.getAvatarUrl())
                .likeCount(article.getLikeCount())
                .viewCount(article.getViewCount())
                .isLiked(isLiked)
                .isBookmarked(isCollected)
                .tags(article.getTags())
                .comments(List.of()) // 暂时不处理评论
                .commentCount(0) // 暂时不处理评论
                .publishTime(article.getCreateTime())
                .build();
    }

    @Override
    @Transactional
    public List<ArticleVO> getHotArticles() {
        // 获取热门文章列表
        List<Article> hotArticles = articleMapper.getHotArticles();
        if (hotArticles == null || hotArticles.isEmpty()) {
            // 如果没有找到热门文章，返回空列表
            return List.of();
        }

        // 获取用户ID
        Long userId = 1L; // 假设当前用户ID为1，实际应用中应从上下文或请求中获取

        // 获取用户收藏的文章ID列表
        List<Long> collectedArticles = articleCollectionRelationMapper.getCollectedArticleIdsByUserId(userId);
        // 获取用户点赞的文章ID列表
        List<Long> likedArticles = articleLikeRelationMapper.getLikedArticleIdsByUserId(userId);

        // 将Article转换为ArticleVO
        return hotArticles.stream()
                .map(article -> {
                    boolean isCollected = collectedArticles.contains(article.getId());
                    boolean isLiked = likedArticles.contains(article.getId());

                    User articleAuthor = userMapper.getUserById(article.getUserId());
                    return ArticleVO.builder()
                            .id(String.valueOf(article.getId()))
                            .title(article.getTitle())
                            .description(article.getDescription())
                            .content(article.getContent())
                            .coverImageUrl(article.getCoverImageUrl())
                            .authorId(String.valueOf(article.getUserId()))
                            .authorName(articleAuthor.getName())
                            .authorAvatarUrl(articleAuthor.getAvatarUrl())
                            .likeCount(article.getLikeCount())
                            .viewCount(article.getViewCount())
                            .isLiked(isLiked)
                            .isBookmarked(isCollected)
                            .tags(article.getTags())
                            .comments(List.of()) // 暂时不处理评论
                            .commentCount(0) // 暂时不处理评论
                            .publishTime(article.getCreateTime())
                            .build();
                })
                .toList();
    }
}
