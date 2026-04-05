-- C 端账号启用状态：0 停用不可登录，1 启用
ALTER TABLE `user`
    ADD COLUMN `status` tinyint NOT NULL DEFAULT 1 COMMENT '1启用 0停用' AFTER `followers_count`;
