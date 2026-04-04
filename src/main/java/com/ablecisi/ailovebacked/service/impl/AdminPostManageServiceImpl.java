package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.PostMapper;
import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminPostManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPostManageServiceImpl implements AdminPostManageService {

    private final PostMapper postMapper;

    @Override
    public PageResult<Post> page(int page, int size) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        long total = postMapper.countAll();
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<Post> records = postMapper.selectPage(offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public Post get(Long id) {
        Post po = postMapper.selectById(id);
        if (po == null) {
            throw new BaseException("帖子不存在");
        }
        return po;
    }

    @Override
    @Transactional
    public long create(Post post) {
        if (post.getUserId() == null) {
            throw new BaseException("userId 不能为空");
        }
        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new BaseException("内容不能为空");
        }
        if (post.getViewCount() == null) {
            post.setViewCount(0);
        }
        if (post.getLikeCount() == null) {
            post.setLikeCount(0);
        }
        if (post.getShareCount() == null) {
            post.setShareCount(0);
        }
        if (post.getCommentCount() == null) {
            post.setCommentCount(0);
        }
        postMapper.insert(post);
        if (post.getId() == null) {
            throw new BaseException("创建失败");
        }
        return post.getId();
    }

    @Override
    @Transactional
    public void update(Long id, Post post) {
        if (postMapper.selectById(id) == null) {
            throw new BaseException("帖子不存在");
        }
        post.setId(id);
        postMapper.updateRow(post);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (postMapper.deleteById(id) == 0) {
            throw new BaseException("帖子不存在");
        }
    }
}
