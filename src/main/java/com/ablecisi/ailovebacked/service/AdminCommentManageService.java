package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminCommentListVO;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminCommentManageService {
    PageResult<AdminCommentListVO> page(int page, int size, String keyword, Long articleId, Long postId);

    void softDelete(long id);

    Comment getEntity(long id);

    void updateEntity(long id, Comment row);

    void deleteHard(long id);
}
