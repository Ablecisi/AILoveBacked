package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostShareDTO {
    @NotNull
    private Long postId;
}
