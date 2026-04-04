package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChatSendDTO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSendDTO {
    @NotNull
    private Long conversationId;
    /**
     * 由服务端从 JWT 注入，请求体可不传
     */
    private Long userId;
    @NotNull
    private Long characterId;
    @NotBlank
    private String text;
}
