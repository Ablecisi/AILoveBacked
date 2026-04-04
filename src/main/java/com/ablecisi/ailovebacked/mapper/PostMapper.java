package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {

    Post selectById(@Param("id") Long id);

    List<Post> selectPage(@Param("offset") int offset, @Param("size") int size);

    long countAll();

    int insert(Post row);

    int updateRow(Post row);

    int deleteById(@Param("id") Long id);
}
