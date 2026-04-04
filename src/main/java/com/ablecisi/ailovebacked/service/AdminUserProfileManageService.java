package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminUserProfileManageService {
    PageResult<UserProfile> page(int page, int size);

    UserProfile get(Long userId);

    void save(UserProfile profile);

    void delete(Long userId);
}
