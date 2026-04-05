/*
 Navicat Premium Dump SQL

 Source Server         : ablecisi
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : ailianlian

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 21/08/2025 19:38:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_character
-- ----------------------------
DROP TABLE IF EXISTS `ai_character`;
CREATE TABLE `ai_character`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `user_id`      bigint                                                        NULL     DEFAULT NULL COMMENT '角色归属用户（可选，系统角色为空）',
    `name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '角色名称',
    `type_id`      bigint                                                        NULL     DEFAULT NULL COMMENT '类型ID（关联 type 表）',
    `type_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '角色类型编码（friend/lover/mentor/coach/assistant）',
    `gender`       tinyint                                                       NULL     DEFAULT 2 COMMENT '性别 0男 1女 2其他/未知',
    `age`          int                                                           NULL     DEFAULT NULL COMMENT '年龄（可选）',
    `image_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '角色头像',
    `traits`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '性格特点（短语逗号分隔：温柔,理性,细心）',
    `persona_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '性格描述（长文本，自我介绍/说话风格）',
    `interests`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '兴趣爱好（逗号分隔或JSON）',
    `backstory`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '背景故事（长文本）',
    `online`       tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否在线：1在线 0离线',
    `status`       tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态：1启用 0下线',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_ai_char_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_ai_char_type` (`type_id` ASC) USING BTREE,
    INDEX `idx_ai_char_status` (`status` ASC) USING BTREE,
    CONSTRAINT `fk_ai_char_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI 角色定义'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
    `description`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '描述',
    `content`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '内容',
    `cover_image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '封面URL',
    `user_id`         bigint                                                        NOT NULL COMMENT '作者ID',
    `view_count`      int                                                           NOT NULL DEFAULT 0 COMMENT '浏览数量',
    `like_count`      int                                                           NOT NULL DEFAULT 0 COMMENT '点赞数量',
    `tags`            json                                                          NULL COMMENT '标签列表',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_article_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_article_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_collect_relation
-- ----------------------------
DROP TABLE IF EXISTS `article_collect_relation`;
CREATE TABLE `article_collect_relation`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '收藏表ID',
    `article_id`  bigint   NOT NULL COMMENT '文章ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_article_collect_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_article_collect_article` (`article_id` ASC) USING BTREE,
    CONSTRAINT `fk_article_collect_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_article_collect_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏文章关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_like_relation
-- ----------------------------
DROP TABLE IF EXISTS `article_like_relation`;
CREATE TABLE `article_like_relation`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `article_id`  bigint   NOT NULL COMMENT '文章ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_article_like_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_article_like_article` (`article_id` ASC) USING BTREE,
    CONSTRAINT `fk_article_like_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_article_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户点赞文章关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `content`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '内容',
    `post_id`     bigint                                                        NULL     DEFAULT NULL COMMENT '所属帖子ID(如果有)',
    `article_id`  bigint                                                        NULL     DEFAULT NULL COMMENT '所属文章ID(如果有)',
    `user_id`     bigint                                                        NOT NULL COMMENT '所属用户ID',
    `like_count`  int                                                           NOT NULL DEFAULT 0 COMMENT '点赞数',
    `reply_count` int                                                           NOT NULL DEFAULT 0 COMMENT '直接子回复数',
    `parent_id`   bigint                                                        NULL     DEFAULT NULL COMMENT '父评论ID(如果有)',
    `root_id`     bigint                                                        NULL     DEFAULT NULL COMMENT '根评论ID（顶层=自身ID）',
    `path`        varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '物化路径：形如 000001/000123/000888',
    `depth`       int                                                           NOT NULL DEFAULT 0 COMMENT '层级：顶层=0，子=1，…',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint                                                       NOT NULL DEFAULT 0 COMMENT '软删',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_comment_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_comment_post` (`post_id` ASC) USING BTREE,
    INDEX `idx_comment_article` (`article_id` ASC) USING BTREE,
    INDEX `idx_comment_root_path` (`root_id` ASC, `path` ASC) USING BTREE,
    INDEX `idx_comment_post_root` (`post_id` ASC, `root_id` ASC, `path` ASC) USING BTREE,
    INDEX `idx_comment_article_root` (`article_id` ASC, `root_id` ASC, `path` ASC) USING BTREE,
    INDEX `idx_comment_parent` (`parent_id` ASC) USING BTREE,
    INDEX `idx_comment_user_time` (`user_id` ASC, `create_time` ASC) USING BTREE,
    CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment_like_relation
-- ----------------------------
DROP TABLE IF EXISTS `comment_like_relation`;
CREATE TABLE `comment_like_relation`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `comment_id`  bigint   NOT NULL COMMENT '评论ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_comment_like_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_comment_like_comment` (`comment_id` ASC) USING BTREE,
    CONSTRAINT `fk_comment_like_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comment_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户点赞评论关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for conversation
-- ----------------------------
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '会话ID',
    `user_id`      bigint                                                        NOT NULL COMMENT '用户ID',
    `character_id` bigint                                                        NOT NULL COMMENT '绑定的AI角色ID',
    `title`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '会话标题',
    `last_message` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '最后消息摘要（保留你的字段，并加长）',
    `last_msg_at`  datetime                                                      NULL     DEFAULT NULL COMMENT '最后消息时间',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_conv_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_conv_char` (`character_id` ASC) USING BTREE,
    INDEX `idx_conv_last_time` (`last_msg_at` ASC) USING BTREE,
    CONSTRAINT `fk_conv_character` FOREIGN KEY (`character_id`) REFERENCES `ai_character` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_conv_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '会话'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for emotion_log
-- ----------------------------
DROP TABLE IF EXISTS `emotion_log`;
CREATE TABLE `emotion_log`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint                                                       NOT NULL COMMENT '用户ID',
    `message_id`  bigint                                                       NOT NULL COMMENT '关联消息ID',
    `emotion`     varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '情绪',
    `confidence`  decimal(4, 3)                                                NOT NULL COMMENT '置信度',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_emolog_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_emolog_msg` (`message_id` ASC) USING BTREE,
    CONSTRAINT `fk_emolog_msg` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_emolog_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '情绪识别日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for follow_relation
-- ----------------------------
DROP TABLE IF EXISTS `follow_relation`;
CREATE TABLE `follow_relation`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `following_id` bigint   NOT NULL COMMENT '关注者的ID',
    `follower_id`  bigint   NOT NULL COMMENT '被关注者的ID',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_follow_from` (`following_id` ASC) USING BTREE,
    INDEX `idx_follow_to` (`follower_id` ASC) USING BTREE,
    CONSTRAINT `fk_follow_from` FOREIGN KEY (`following_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_follow_to` FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '关注关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`              bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `conversation_id` bigint                                                       NOT NULL COMMENT '会话ID',
    `user_id`         bigint                                                       NOT NULL COMMENT '归属用户（冗余存储便于查询）',
    `type`            tinyint                                                      NOT NULL COMMENT '0 发送(用户) 1 接收(AI)',
    `content`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci    NOT NULL COMMENT '消息内容',
    `emotion`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '情绪标签（neutral/angry/happy/sad/fear/surprise 等）',
    `confidence`      decimal(4, 3)                                                NULL     DEFAULT NULL COMMENT '情绪置信度 0~1',
    `is_read`         tinyint                                                      NOT NULL DEFAULT 0 COMMENT '0未读 1已读',
    `create_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_msg_conv` (`conversation_id` ASC) USING BTREE,
    INDEX `idx_msg_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_msg_time` (`create_time` ASC) USING BTREE,
    CONSTRAINT `fk_msg_conv` FOREIGN KEY (`conversation_id`) REFERENCES `conversation` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_msg_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '消息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '帖子ID',
    `content`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci     NOT NULL COMMENT '内容',
    `image_urls`  json                                                          NULL COMMENT '帖子图片地址列表',
    `user_id`     bigint                                                        NOT NULL COMMENT '作者ID',
    `tags`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '帖子标签列表',
    `like_count`  int                                                           NULL     DEFAULT 0 COMMENT '点赞数量',
    `share_count` int                                                           NULL     DEFAULT 0 COMMENT '分享数量',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_post_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '帖子'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post_like_relation
-- ----------------------------
DROP TABLE IF EXISTS `post_like_relation`;
CREATE TABLE `post_like_relation`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `post_id`     bigint   NOT NULL COMMENT '帖子ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_post_like_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_post_like_post` (`post_id` ASC) USING BTREE,
    CONSTRAINT `fk_post_like_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_post_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户点赞帖子关系'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for prompt_template
-- ----------------------------
DROP TABLE IF EXISTS `prompt_template`;
CREATE TABLE `prompt_template`
(
    `id`          bigint                                                       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_code`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色类型编码（gentle/cheerful/rational/mentor/lover/coach等）',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '模板名称',
    `template`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NOT NULL COMMENT '模板文本（带占位符）',
    `status`      tinyint                                                      NOT NULL DEFAULT 1 COMMENT '1启用 0下线',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_prompt_role` (`role_code` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Prompt 模板'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`
(
    `id`           bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '类型ID',
    `name`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型称谓',
    `prompt_style` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NULL COMMENT '对话语气/风格描述（填入系统提示 {style}；空则用全局默认）',
    `create_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '类型字典'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '账号',
    `password`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '123456' COMMENT '密码（建议加密存储）',
    `name`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '昵称',
    `description`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '描述',
    `avatar_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '头像URL',
    `following_count` int                                                           NULL     DEFAULT 0 COMMENT '关注数量',
    `followers_count` int                                                           NULL     DEFAULT 0 COMMENT '粉丝数量',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_username` (`username` ASC) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`
(
    `user_id`         bigint                                                         NOT NULL COMMENT '用户ID',
    `interests`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL     DEFAULT NULL COMMENT '画像：兴趣（JSON或逗号分隔字符串）',
    `tone_preference` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL     DEFAULT NULL COMMENT '画像：偏好语气',
    `emotion_stats`   varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL     DEFAULT NULL COMMENT '画像：情绪统计JSON {\"happy\":12,\"sad\":7,...}',
    `update_time`     datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户画像'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 运营后台管理员（与 C 端 user 表独立）
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`
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

-- 应用远程配置（bootstrap，无 JWT 也可读）
DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config`
(
    `config_key`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
    `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        NULL COMMENT '配置值（可为 JSON 字符串）',
    `update_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`config_key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户端 bootstrap 键值'
  ROW_FORMAT = Dynamic;

INSERT INTO `app_config` (`config_key`, `config_value`)
VALUES ('bootstrap.version', '1'),
       ('minAppVersion', '1'),
       ('home.featured.enabled', 'true');

-- 用户按日活跃（DAU）：当日任意一次 App 访问写入一条，(user_id, active_date) 唯一
DROP TABLE IF EXISTS `user_active_day`;
CREATE TABLE `user_active_day`
(
    `user_id`       bigint   NOT NULL COMMENT '用户ID',
    `active_date`   date     NOT NULL COMMENT '自然日（服务器按 Asia/Shanghai）',
    `first_seen_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '当日首次记录时间',
    PRIMARY KEY (`user_id`, `active_date`) USING BTREE,
    INDEX `idx_uad_date` (`active_date` ASC) USING BTREE,
    CONSTRAINT `fk_uad_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户日活'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
