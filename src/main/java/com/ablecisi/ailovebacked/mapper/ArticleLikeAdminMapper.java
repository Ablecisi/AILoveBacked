package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.ArticleLikeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleLikeAdminMapper {

    ArticleLikeRelation selectById(@Param("id") Long id);

    long countAll();

    List<ArticleLikeRelation> selectPage(@Param("offset") int offset, @Param("size") int size);

    int insert(ArticleLikeRelation row);

    int updateRow(ArticleLikeRelation row);

    int deleteById(@Param("id") Long id);

    ArticleLikeRelation selectByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int deleteByUserAndArticle(@Param("userId") Long userId, @Param("articleId") Long articleId);
}
