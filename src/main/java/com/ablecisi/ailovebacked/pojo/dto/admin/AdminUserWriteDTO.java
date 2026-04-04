package com.ablecisi.ailovebacked.pojo.dto.admin;

import lombok.Data;

@Data
public class AdminUserWriteDTO {
    private String username;
    /**
     * 新建必填；更新时非空则重置密码（BCrypt）
     */
    private String password;
    private String name;
    private String description;
    private String avatarUrl;
    private Integer followingCount;
    private Integer followersCount;
}
