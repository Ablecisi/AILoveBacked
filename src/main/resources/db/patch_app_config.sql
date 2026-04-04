-- 已有库手工执行：新增 bootstrap 配置表（若已存在可跳过）
CREATE TABLE IF NOT EXISTS `app_config`
(
    `config_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
    `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NULL COMMENT '配置值（可为 JSON 字符串）',
    `update_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户端 bootstrap 键值'
  ROW_FORMAT = Dynamic;

INSERT IGNORE INTO `app_config` (`config_key`, `config_value`)
VALUES ('bootstrap.version', '1'),
       ('minAppVersion', '1'),
       ('home.featured.enabled', 'true');
