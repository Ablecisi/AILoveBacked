package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/28
 * 星期四
 * 23:48
 **/
@Mapper
public interface CommentMapper {

    // ===== 写入/回填树 =====
    int insertBase(Comment c); // useGeneratedKeys

    int fillTreeForRoot(@Param("id") Long id);

    int fillTreeForChild(@Param("id") Long id,
                         @Param("rootId") Long rootId,
                         @Param("path") String path,
                         @Param("depth") int depth);

    Comment selectTreeByIdForUpdate(@Param("id") Long id);

    Comment selectById(@Param("id") Long id);

    int incReplyCount(@Param("id") Long id);

    int decReplyCount(@Param("id") Long id); // 暂未使用，预留

    // ===== 查询（带JOIN user映射到 VO）=====
    List<CommentVO> pageTopByArticle(@Param("articleId") Long articleId,
                                     @Param("sort") String sort,
                                     @Param("offset") int offset,
                                     @Param("size") int size);

    long countTopByArticle(@Param("articleId") Long articleId);

    List<CommentVO> pageTopByPost(@Param("postId") Long postId,
                                  @Param("sort") String sort,
                                  @Param("offset") int offset,
                                  @Param("size") int size);

    long countTopByPost(@Param("postId") Long postId);

    List<CommentVO> listTreeByRoot(@Param("rootId") Long rootId,
                                   @Param("afterPath") String afterPath,
                                   @Param("size") int size);

    List<CommentVO> pageChildren(@Param("parentId") Long parentId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    long countChildren(@Param("parentId") Long parentId);

    // ===== 修改/删除 =====
    int updateContent(@Param("id") Long id, @Param("content") String content);

    int softDelete(@Param("id") Long id);

    // ===== 点赞 =====
    int insertLike(@Param("userId") Long userId, @Param("commentId") Long commentId);

    int deleteLike(@Param("userId") Long userId, @Param("commentId") Long commentId);

    int incLikeCount(@Param("id") Long id);

    int decLikeCount(@Param("id") Long id);

    // ★ 新增：按ID返回 CommentVO（JOIN user）
    CommentVO selectVOById(@Param("id") Long id);

    // 方案A：批量查这一页所有顶层的子孙（一次SQL；depth不限可传null）
    List<CommentVO> listDescendantsByRoots(@Param("rootIds") List<Long> rootIds,
                                           @Param("maxDepth") Integer maxDepth);
}
