package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateDTO {
    @NotBlank
    private String content;
    private List<String> imageUrls;
    private List<String> tags;
}
