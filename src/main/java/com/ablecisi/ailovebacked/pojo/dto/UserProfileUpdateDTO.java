package com.ablecisi.ailovebacked.pojo.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    private String name;
    private String description;
    private String avatarUrl;
}
