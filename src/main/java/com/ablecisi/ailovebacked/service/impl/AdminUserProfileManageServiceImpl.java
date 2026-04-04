package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.UserProfileMapper;
import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminUserProfileManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserProfileManageServiceImpl implements AdminUserProfileManageService {

    private final UserProfileMapper userProfileMapper;

    @Override
    public PageResult<UserProfile> page(int page, int size) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        long total = userProfileMapper.countAll();
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<UserProfile> records = userProfileMapper.selectPage(offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public UserProfile get(Long userId) {
        UserProfile p = userProfileMapper.selectByUserId(userId);
        if (p == null) {
            throw new BaseException("画像不存在");
        }
        return p;
    }

    @Override
    @Transactional
    public void save(UserProfile profile) {
        if (profile.getUserId() == null) {
            throw new BaseException("userId 不能为空");
        }
        UserProfile existing = userProfileMapper.selectByUserId(profile.getUserId());
        if (existing == null) {
            userProfileMapper.insert(profile);
        } else {
            userProfileMapper.update(profile);
        }
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (userProfileMapper.deleteByUserId(userId) == 0) {
            throw new BaseException("画像不存在");
        }
    }
}
