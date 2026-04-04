package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.PostLikeRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostLikeAdminMapper {

    PostLikeRelation selectById(@Param("id") Long id);

    long countAll();

    List<PostLikeRelation> selectPage(@Param("offset") int offset, @Param("size") int size);

    int insert(PostLikeRelation row);

    int updateRow(PostLikeRelation row);

    int deleteById(@Param("id") Long id);
}
