package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptDraftRequestDTO;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptTemplateDTO;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;

import java.util.List;

public interface AdminPromptManageService {
    List<PromptTemplate> listAll();

    PromptTemplate getById(long id);

    long create(AdminPromptTemplateDTO dto);

    void update(long id, AdminPromptTemplateDTO dto);

    /**
     * 调用 LLM 根据场景与占位符选项生成模板草稿（不落库）。
     */
    String draftTemplate(AdminPromptDraftRequestDTO request);
}
