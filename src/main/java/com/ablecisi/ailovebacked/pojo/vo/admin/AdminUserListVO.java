package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserListVO {
    private Long id;
    private String username;
    private String name;
    private String description;
    private String avatarUrl;
    private Integer followingCount;
    private Integer followersCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
