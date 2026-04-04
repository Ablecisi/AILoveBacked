package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminUserWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserDetailVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminUserListVO;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminUserManageService {
    PageResult<AdminUserListVO> page(int page, int size, String keyword);

    AdminUserDetailVO getDetail(Long id);

    long create(AdminUserWriteDTO dto);

    void update(Long id, AdminUserWriteDTO dto);

    void delete(Long id);
}
