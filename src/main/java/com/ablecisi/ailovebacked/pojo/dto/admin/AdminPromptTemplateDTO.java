package com.ablecisi.ailovebacked.pojo.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminPromptTemplateDTO {
    @NotBlank
    private String roleType;
    private String name;
    @NotBlank
    private String template;
    @NotNull
    private Integer status;
}
