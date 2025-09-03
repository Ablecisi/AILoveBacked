package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.CreateCommentDTO;
import com.ablecisi.ailovebacked.pojo.dto.UpdateCommentDTO;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.pojo.vo.RootTreeVO;
import com.ablecisi.ailovebacked.result.PageResult;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/28
 * 星期四
 * 23:53
 **/
public interface CommentService {
    CommentVO create(CreateCommentDTO dto);

    List<CommentVO> pageTopByArticle(Long articleId, String sort, int page, int size);

    long countTopByArticle(Long articleId);

    List<CommentVO> pageTopByPost(Long postId, String sort, int page, int size);

    long countTopByPost(Long postId);

    List<CommentVO> listTreeByRoot(Long rootId, String afterPath, int size);

    List<CommentVO> pageChildren(Long parentId, int page, int size);

    long countChildren(Long parentId);

    int updateContent(UpdateCommentDTO dto);

    int softDelete(Long id);

    boolean like(Long userId, Long commentId);

    boolean unlike(Long userId, Long commentId);

    // 方案A：顶层分页 + 批量子树打包
    PageResult<RootTreeVO> bundleByTarget(String targetType, Long targetId, String sort, int page, int size, Integer maxDepth);
}
