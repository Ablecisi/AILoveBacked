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

    /**
     * 新增一条评论时 post.comment_count +1
     */
    int incrementCommentCount(@Param("id") Long id);

    /**
     * 软删除一条评论时 post.comment_count -1（不低于 0）
     */
    int decrementCommentCount(@Param("id") Long id);

    int adjustCommentCount(@Param("id") Long id, @Param("delta") int delta);

    int incrementLikeCount(@Param("id") Long id);

    int decrementLikeCount(@Param("id") Long id);

    int incrementShareCount(@Param("id") Long id);

    int incrementViewCount(@Param("id") Long id);
}
