CREATE TABLE IF NOT EXISTS user
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname   VARCHAR(50),
    avatar     VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_character
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT      NOT NULL,
    name         VARCHAR(50) NOT NULL,
    type         VARCHAR(32) NOT NULL,
    gender       VARCHAR(16) NOT NULL,
    traits       VARCHAR(255),
    persona_desc TEXT,
    interests    TEXT,
    backstory    TEXT,
    status       TINYINT  DEFAULT 1,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_type (type),
    INDEX idx_status (status)
);

CREATE TABLE IF NOT EXISTS conversation
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT NOT NULL,
    character_id BIGINT,
    title        VARCHAR(100),
    last_msg_at  DATETIME,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_char (character_id)
);

CREATE TABLE IF NOT EXISTS message
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT             NOT NULL,
    sender          ENUM ('USER','AI') NOT NULL,
    content         TEXT               NOT NULL,
    emotion         VARCHAR(16),
    confidence      DECIMAL(4, 3),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_conv (conversation_id),
    INDEX idx_time (created_at)
);

CREATE TABLE IF NOT EXISTS user_profile
(
    user_id         BIGINT PRIMARY KEY,
    interests       JSON        NULL,
    tone_preference VARCHAR(32) NULL,
    emotion_stats   JSON        NULL,
    updated_at      DATETIME
);

CREATE TABLE IF NOT EXISTS prompt_template
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(32) NOT NULL,
    name      VARCHAR(64),
    template  TEXT        NOT NULL,
    status    TINYINT DEFAULT 1
);
