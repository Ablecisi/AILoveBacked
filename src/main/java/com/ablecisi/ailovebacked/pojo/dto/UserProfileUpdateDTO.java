package com.ablecisi.ailovebacked.pojo.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    /**
     * 登录名，修改时需校验唯一
     */
    private String username;
    private String name;
    private String description;
    private String avatarUrl;
}
