package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminPostManageService {
    PageResult<Post> page(int page, int size);

    Post get(Long id);

    long create(Post post);

    void update(Long id, Post post);

    void delete(Long id);
}
