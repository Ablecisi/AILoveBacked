package com.ablecisi.ailovebacked.pojo.dto.admin;

import lombok.Data;

@Data
public class AdminAiCharacterWriteDTO {
    private Long userId;
    private String name;
    private Long typeId;
    private Integer gender;
    private Integer age;
    private String imageUrl;
    private String traits;
    private String personaDesc;
    private String interests;
    private String backstory;
    private Integer online;
    private Integer status;
}
