-- 已有库增量：用户日活表（执行一次即可）
CREATE TABLE IF NOT EXISTS `user_active_day`
(
    `user_id`       bigint   NOT NULL COMMENT '用户ID',
    `active_date`   date     NOT NULL COMMENT '自然日（Asia/Shanghai）',
    `first_seen_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '当日首次记录时间',
    PRIMARY KEY (`user_id`, `active_date`) USING BTREE,
    INDEX `idx_uad_date` (`active_date` ASC) USING BTREE,
    CONSTRAINT `fk_uad_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='用户日活';
