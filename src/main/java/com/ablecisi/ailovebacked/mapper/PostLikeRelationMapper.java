package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.PostLikeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeRelationMapper {

    int countByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    int insert(PostLikeRelation row);

    int deleteByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
}
