package com.ablecisi.ailovebacked.pojo.dto.admin;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 由运营描述场景与可选占位符，调用 LLM 生成 Prompt 模板草稿（仍须人工保存）。
 */
@Data
public class AdminPromptDraftRequestDTO {
    /**
     * 业务场景 / 需求描述
     */
    private String scenario;
    /**
     * 角色类型提示（与 type.name / role_code 对齐）
     */
    private String roleType;
    /**
     * 需要出现在模板中的占位符名，不含花括号，如 name、emotion
     */
    private List<String> placeholders;
    /**
     * 各字段的补充说明（可选）
     */
    private Map<String, String> fieldHints;
    /**
     * 用户侧个性化 / 语气等自由文本
     */
    private String userPersonaNotes;
}
