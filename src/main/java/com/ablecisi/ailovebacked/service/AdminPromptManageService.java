package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptTemplateDTO;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;

import java.util.List;

public interface AdminPromptManageService {
    List<PromptTemplate> listAll();

    PromptTemplate getById(long id);

    long create(AdminPromptTemplateDTO dto);

    void update(long id, AdminPromptTemplateDTO dto);
}
