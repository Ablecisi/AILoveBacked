-- 角色类型表：对话语气/风格（与角色查询 JOIN 带出，供 PromptService {style} 使用）
ALTER TABLE `type`
    ADD COLUMN `prompt_style` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL
        COMMENT '对话语气/风格描述（填入系统提示 {style}；空则用全局默认）' AFTER `name`;
