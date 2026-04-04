package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.FollowRelation;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminFollowManageService {
    PageResult<FollowRelation> page(int page, int size);

    FollowRelation get(Long id);

    long create(FollowRelation row);

    void update(Long id, FollowRelation row);

    void delete(Long id);
}
