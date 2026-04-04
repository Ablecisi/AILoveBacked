package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.AdminMapper;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminOpsAccountWriteDTO;
import com.ablecisi.ailovebacked.pojo.entity.AdminUser;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminOpsAccountVO;
import com.ablecisi.ailovebacked.service.AdminOpsUserManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOpsUserManageServiceImpl implements AdminOpsUserManageService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AdminOpsAccountVO> listAll() {
        return adminMapper.selectAll().stream().map(this::toVo).toList();
    }

    private AdminOpsAccountVO toVo(AdminUser u) {
        AdminOpsAccountVO v = new AdminOpsAccountVO();
        v.setId(u.getId());
        v.setUsername(u.getUsername());
        v.setCreateTime(u.getCreateTime());
        v.setUpdateTime(u.getUpdateTime());
        return v;
    }

    @Override
    public AdminOpsAccountVO get(Long id) {
        AdminUser u = adminMapper.selectById(id);
        if (u == null) {
            throw new BaseException("账号不存在");
        }
        return toVo(u);
    }

    @Override
    @Transactional
    public long create(AdminOpsAccountWriteDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new BaseException("登录名不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BaseException("密码不能为空");
        }
        String username = dto.getUsername().trim();
        if (adminMapper.selectByUsername(username) != null) {
            throw new BaseException("登录名已存在");
        }
        AdminUser u = new AdminUser();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(dto.getPassword()));
        adminMapper.insert(u);
        if (u.getId() == null) {
            throw new BaseException("创建失败");
        }
        return u.getId();
    }

    @Override
    @Transactional
    public void update(Long id, AdminOpsAccountWriteDTO dto) {
        AdminUser u = adminMapper.selectById(id);
        if (u == null) {
            throw new BaseException("账号不存在");
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            String nu = dto.getUsername().trim();
            AdminUser other = adminMapper.selectByUsername(nu);
            if (other != null && !other.getId().equals(id)) {
                throw new BaseException("登录名已被占用");
            }
            adminMapper.updateUsername(id, nu);
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            adminMapper.updatePassword(id, passwordEncoder.encode(dto.getPassword()));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (adminMapper.count() <= 1) {
            throw new BaseException("至少保留一个运营账号");
        }
        if (adminMapper.deleteById(id) == 0) {
            throw new BaseException("账号不存在");
        }
    }
}
