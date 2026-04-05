package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminAiCharacterWriteDTO;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminAiCharacterManageService {
    PageResult<AiCharacterVO> page(int page, int size, String keyword, Long typeId, Integer status, Integer online);

    AiCharacterVO get(Long id);

    long create(AdminAiCharacterWriteDTO dto);

    void update(Long id, AdminAiCharacterWriteDTO dto);

    void delete(Long id);
}
