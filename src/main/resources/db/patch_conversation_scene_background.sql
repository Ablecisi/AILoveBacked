-- 会话场景/背景（创建会话时由用户填写，参与 Prompt 渲染）
ALTER TABLE conversation
    ADD COLUMN scene_background TEXT NULL COMMENT '会话场景与背景' AFTER title;
