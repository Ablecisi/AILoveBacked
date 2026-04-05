package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.ArticleCollectRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleCollectAdminMapper {

    ArticleCollectRelation selectById(@Param("id") Long id);

    long countAll();

    List<ArticleCollectRelation> selectPage(@Param("offset") int offset, @Param("size") int size);

    int insert(ArticleCollectRelation row);

    int updateRow(ArticleCollectRelation row);

    int deleteById(@Param("id") Long id);

    ArticleCollectRelation selectByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int deleteByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
