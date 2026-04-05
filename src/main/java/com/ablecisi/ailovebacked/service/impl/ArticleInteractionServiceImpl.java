package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.ArticleCollectAdminMapper;
import com.ablecisi.ailovebacked.mapper.ArticleLikeAdminMapper;
import com.ablecisi.ailovebacked.mapper.ArticleMapper;
import com.ablecisi.ailovebacked.pojo.entity.ArticleCollectRelation;
import com.ablecisi.ailovebacked.pojo.entity.ArticleLikeRelation;
import com.ablecisi.ailovebacked.service.ArticleInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleInteractionServiceImpl implements ArticleInteractionService {

    private final ArticleMapper articleMapper;
    private final ArticleLikeAdminMapper articleLikeAdminMapper;
    private final ArticleCollectAdminMapper articleCollectAdminMapper;

    @Override
    @Transactional
    public void setArticleLiked(long userId, long articleId, boolean like) {
        if (articleMapper.selectById(articleId) == null) {
            throw new BaseException("文章不存在");
        }
        var existing = articleLikeAdminMapper.selectByUserAndArticle(userId, articleId);
        if (like) {
            if (existing != null) {
                return;
            }
            articleLikeAdminMapper.insert(ArticleLikeRelation.builder()
                    .userId(userId)
                    .articleId(articleId)
                    .build());
            articleMapper.adjustLikeCount(articleId, 1);
        } else {
            if (existing == null) {
                return;
            }
            articleLikeAdminMapper.deleteByUserAndArticle(userId, articleId);
            articleMapper.adjustLikeCount(articleId, -1);
        }
    }

    @Override
    @Transactional
    public void setArticleCollected(long userId, long articleId, boolean collect) {
        if (articleMapper.selectById(articleId) == null) {
            throw new BaseException("文章不存在");
        }
        var existing = articleCollectAdminMapper.selectByUserAndArticle(userId, articleId);
        if (collect) {
            if (existing != null) {
                return;
            }
            articleCollectAdminMapper.insert(ArticleCollectRelation.builder()
                    .userId(userId)
                    .articleId(articleId)
                    .build());
        } else {
            if (existing == null) {
                return;
            }
            articleCollectAdminMapper.deleteByUserAndArticle(userId, articleId);
        }
    }
}
