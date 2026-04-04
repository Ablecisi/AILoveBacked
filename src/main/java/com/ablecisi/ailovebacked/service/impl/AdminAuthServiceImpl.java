package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.AdminMapper;
import com.ablecisi.ailovebacked.pojo.dto.AdminLoginDTO;
import com.ablecisi.ailovebacked.pojo.entity.AdminUser;
import com.ablecisi.ailovebacked.pojo.vo.AdminLoginVO;
import com.ablecisi.ailovebacked.properties.AdminJwtProperties;
import com.ablecisi.ailovebacked.service.AdminAuthService;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminJwtProperties adminJwtProperties;

    @Override
    public AdminLoginVO login(AdminLoginDTO dto) {
        AdminUser user = adminMapper.selectByUsername(dto.getUsername().trim());
        if (user == null) {
            throw new BaseException("账号或密码错误");
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BaseException("账号或密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, user.getUsername());
        claims.put("adminId", user.getId());
        String token = JwtUtil.createJWT(
                adminJwtProperties.getJwtSecretKey(),
                adminJwtProperties.getTtlMs(),
                claims);
        return AdminLoginVO.builder()
                .token(token)
                .username(user.getUsername())
                .build();
    }
}
