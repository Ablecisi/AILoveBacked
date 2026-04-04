package com.ablecisi.ailovebacked.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUser {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
