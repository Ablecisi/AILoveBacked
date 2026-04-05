package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.PromptTemplateMapper;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptDraftRequestDTO;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminPromptTemplateDTO;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import com.ablecisi.ailovebacked.service.AdminPromptManageService;
import com.ablecisi.ailovebacked.service.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPromptManageServiceImpl implements AdminPromptManageService {

    private final PromptTemplateMapper promptTemplateMapper;
    private final LlmClient llmClient;

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

    @Override
    public String draftTemplate(AdminPromptDraftRequestDTO req) {
        if (req.getScenario() == null || req.getScenario().isBlank()) {
            throw new BaseException("请填写场景与需求描述");
        }
        String phLine;
        if (req.getPlaceholders() == null || req.getPlaceholders().isEmpty()) {
            phLine = "{style} {name} {gender} {traits} {persona_desc} {interests} {backstory} "
                    + "{emotion} {confidence} {profile} {dialogue} {user_text} {user_name}";
        } else {
            phLine = req.getPlaceholders().stream()
                    .filter(s -> s != null && !s.isBlank())
                    .map(s -> "{" + s.trim().replace("{", "").replace("}", "") + "}")
                    .collect(Collectors.joining(" "));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("你是资深 Prompt 工程师。请根据下列信息，输出**一段**可直接用于大模型系统提示（system）的模板正文，语言为中文。\n");
        sb.append("硬性要求：\n");
        sb.append("1) 必须使用单花括号占位符，且仅围绕下列变量名（可按需取舍句子，但不要发明新占位符）：")
                .append(phLine).append("\n");
        sb.append("2) 不要输出 Markdown 代码围栏（不要 ```）。\n");
        sb.append("3) 结构清晰：先写 AI 角色设定区块，再写用户与上下文区块，最后写回复约束。\n\n");
        sb.append("【场景与产品需求】\n").append(req.getScenario().trim()).append("\n\n");
        if (req.getRoleType() != null && !req.getRoleType().isBlank()) {
            sb.append("【角色类型编码/名称（与业务 type 对齐）】\n").append(req.getRoleType().trim()).append("\n\n");
        }
        if (req.getUserPersonaNotes() != null && !req.getUserPersonaNotes().isBlank()) {
            sb.append("【用户个性化 / 语气偏好】\n").append(req.getUserPersonaNotes().trim()).append("\n\n");
        }
        if (req.getFieldHints() != null && !req.getFieldHints().isEmpty()) {
            sb.append("【各字段补充说明】\n");
            req.getFieldHints().forEach((k, v) -> {
                if (k != null && v != null && !v.isBlank()) {
                    sb.append("- ").append(k.trim()).append(": ").append(v.trim()).append("\n");
                }
            });
            sb.append("\n");
        }
        sb.append("请直接输出模板正文。");
        return llmClient.generate(sb.toString());
    }
}
