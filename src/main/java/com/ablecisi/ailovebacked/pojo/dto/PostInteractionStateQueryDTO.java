package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PostInteractionStateQueryDTO {
    @NotEmpty
    private List<Long> postIds;
}
