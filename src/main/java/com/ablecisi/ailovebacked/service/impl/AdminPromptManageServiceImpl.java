package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.PromptTemplateMapper;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptTemplateDTO;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import com.ablecisi.ailovebacked.service.AdminPromptManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPromptManageServiceImpl implements AdminPromptManageService {

    private final PromptTemplateMapper promptTemplateMapper;

    @Override
    public List<PromptTemplate> listAll() {
        return promptTemplateMapper.listAllForAdmin();
    }

    @Override
    public PromptTemplate getById(long id) {
        PromptTemplate t = promptTemplateMapper.selectById(id);
        if (t == null) {
            throw new BaseException("模板不存在");
        }
        return t;
    }

    @Override
    public long create(AdminPromptTemplateDTO dto) {
        PromptTemplate row = PromptTemplate.builder()
                .roleType(dto.getRoleType().trim())
                .name(dto.getName() != null ? dto.getName().trim() : null)
                .template(dto.getTemplate())
                .status(normalizeStatus(dto.getStatus()))
                .build();
        promptTemplateMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    public void update(long id, AdminPromptTemplateDTO dto) {
        PromptTemplate existing = promptTemplateMapper.selectById(id);
        if (existing == null) {
            throw new BaseException("模板不存在");
        }
        existing.setRoleType(dto.getRoleType().trim());
        existing.setName(dto.getName() != null ? dto.getName().trim() : null);
        existing.setTemplate(dto.getTemplate());
        existing.setStatus(normalizeStatus(dto.getStatus()));
        promptTemplateMapper.updateById(existing);
    }

    private static int normalizeStatus(Integer status) {
        if (status == null || (status != 0 && status != 1)) {
            throw new BaseException("status 须为 0 或 1");
        }
        return status;
    }
}
