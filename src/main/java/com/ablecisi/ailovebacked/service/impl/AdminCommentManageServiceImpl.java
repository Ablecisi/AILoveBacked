package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.CommentMapper;
import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminCommentListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminCommentManageService;
import com.ablecisi.ailovebacked.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentManageServiceImpl implements AdminCommentManageService {

    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @Override
    public PageResult<AdminCommentListVO> page(int page, int size, String keyword, Long articleId, Long postId) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = commentMapper.countPageForAdmin(kw, articleId, postId);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<AdminCommentListVO> records = commentMapper.pageForAdmin(kw, articleId, postId, offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public void softDelete(long id) {
        int n = commentService.softDelete(id);
        if (n == 0) {
            throw new BaseException("评论不存在或已删除");
        }
    }

    @Override
    public Comment getEntity(long id) {
        Comment c = commentMapper.selectById(id);
        if (c == null) {
            throw new BaseException("评论不存在");
        }
        return c;
    }

    @Override
    public void updateEntity(long id, Comment row) {
        if (commentMapper.selectById(id) == null) {
            throw new BaseException("评论不存在");
        }
        row.setId(id);
        commentMapper.adminUpdateRow(row);
    }

    @Override
    public void deleteHard(long id) {
        if (commentMapper.adminDeleteHard(id) == 0) {
            throw new BaseException("评论不存在");
        }
    }
}
