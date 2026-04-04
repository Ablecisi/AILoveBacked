package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.AdminLoginDTO;
import com.ablecisi.ailovebacked.pojo.vo.AdminLoginVO;

public interface AdminAuthService {
    AdminLoginVO login(AdminLoginDTO dto);
}
