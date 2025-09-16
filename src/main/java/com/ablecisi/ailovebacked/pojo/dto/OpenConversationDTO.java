package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * OpenConversationDTO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:28
 **/
@Data
public class OpenConversationDTO {
    @NotNull
    private Long characterId;
    private String title; // 可选
}
