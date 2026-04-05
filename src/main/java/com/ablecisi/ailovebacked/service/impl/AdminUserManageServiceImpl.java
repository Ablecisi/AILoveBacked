package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminUserWriteDTO;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserDetailVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminUserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserManageServiceImpl implements AdminUserManageService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResult<AdminUserListVO> page(int page, int size, String keyword) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = userMapper.countPageForAdmin(kw);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<AdminUserListVO> records = userMapper.pageForAdmin(kw, offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public AdminUserDetailVO getDetail(Long id) {
        User u = userMapper.getUserById(id);
        if (u == null) {
            throw new BaseException("用户不存在");
        }
        AdminUserDetailVO v = new AdminUserDetailVO();
        v.setId(u.getId());
        v.setUsername(u.getUsername());
        v.setName(u.getName());
        v.setDescription(u.getDescription());
        v.setAvatarUrl(u.getAvatarUrl());
        v.setFollowingCount(u.getFollowingCount());
        v.setFollowersCount(u.getFollowersCount());
        v.setStatus(u.getStatus() != null ? u.getStatus() : 1);
        v.setCreateTime(u.getCreateTime());
        v.setUpdateTime(u.getUpdateTime());
        return v;
    }

    @Override
    @Transactional
    public long create(AdminUserWriteDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new BaseException("用户名不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BaseException("初始密码不能为空");
        }
        String username = dto.getUsername().trim();
        if (userMapper.getUserByUsername(username) != null) {
            throw new BaseException("用户名已存在");
        }
        User u = User.builder()
                .username(username)
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .description(dto.getDescription())
                .avatarUrl(dto.getAvatarUrl())
                .followingCount(dto.getFollowingCount() != null ? dto.getFollowingCount() : 0)
                .followersCount(dto.getFollowersCount() != null ? dto.getFollowersCount() : 0)
                .status(dto.getStatus() != null ? dto.getStatus() : 1)
                .build();
        userMapper.insertUser(u);
        if (u.getId() == null) {
            throw new BaseException("创建失败");
        }
        return u.getId();
    }

    @Override
    @Transactional
    public void update(Long id, AdminUserWriteDTO dto) {
        User u = userMapper.getUserById(id);
        if (u == null) {
            throw new BaseException("用户不存在");
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            String nu = dto.getUsername().trim();
            User other = userMapper.getUserByUsername(nu);
            if (other != null && !other.getId().equals(id)) {
                throw new BaseException("用户名已被占用");
            }
            u.setUsername(nu);
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getName() != null) {
            u.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            u.setDescription(dto.getDescription());
        }
        if (dto.getAvatarUrl() != null) {
            u.setAvatarUrl(dto.getAvatarUrl());
        }
        if (dto.getFollowingCount() != null) {
            u.setFollowingCount(dto.getFollowingCount());
        }
        if (dto.getFollowersCount() != null) {
            u.setFollowersCount(dto.getFollowersCount());
        }
        if (dto.getStatus() != null) {
            u.setStatus(dto.getStatus());
        }
        userMapper.updateUser(u);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (userMapper.deleteUserById(id) == 0) {
            throw new BaseException("用户不存在");
        }
    }
}
