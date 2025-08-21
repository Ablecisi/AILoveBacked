package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.entity
 *
 * @author Ablecisi
 * @version 1.0
 * 2024/12/23
 * 星期一
 * 15:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 用户ID
    private String username; // 用户名
    private String password; // 密码
    private String name; // 昵称
    private String description; // 用户简介
    private String avatarUrl; // 头像URL
    private Integer followingCount; // 关注人数
    private Integer followersCount; // 粉丝人数
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
