-- 可选：写入 app_config 后覆盖 bootstrap 默认值（与 AppBootstrapService 默认一致时可不执行）
INSERT INTO app_config (config_key, config_value)
VALUES ('rateLimit.llm.perMinute', '30'),
       ('rateLimit.emotion.perMinute', '120'),
       ('rateLimit.llm.dailyQuota', '0')
ON DUPLICATE KEY UPDATE config_value = VALUES(config_value),
                        update_time  = CURRENT_TIMESTAMP;
