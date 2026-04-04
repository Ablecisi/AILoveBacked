package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminOpsAccountWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminOpsAccountVO;

import java.util.List;

public interface AdminOpsUserManageService {
    List<AdminOpsAccountVO> listAll();

    AdminOpsAccountVO get(Long id);

    long create(AdminOpsAccountWriteDTO dto);

    void update(Long id, AdminOpsAccountWriteDTO dto);

    void delete(Long id);
}
