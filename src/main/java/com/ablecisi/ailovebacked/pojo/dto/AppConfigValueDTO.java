package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppConfigValueDTO {
    @NotNull
    private String value;
}
