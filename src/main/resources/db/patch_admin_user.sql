-- 已有库执行：管理员表（与 CommandLineRunner 种子 admin/admin123 配合）
CREATE TABLE IF NOT EXISTS `admin_user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '登录名',
    `password`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'BCrypt 密码',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_admin_username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '运营后台管理员'
  ROW_FORMAT = Dynamic;
