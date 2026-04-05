package com.ablecisi.ailovebacked.service;

public interface ArticleInteractionService {

    void setArticleLiked(long userId, long articleId, boolean like);

    void setArticleCollected(long userId, long articleId, boolean collect);
}
