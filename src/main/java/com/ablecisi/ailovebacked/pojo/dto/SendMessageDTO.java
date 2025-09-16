package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * SendMessageDTO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:29
 **/
@Data
public class SendMessageDTO {
    @NotNull
    private Long conversationId;
    @NotBlank
    private String content;
}
