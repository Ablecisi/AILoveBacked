package com.ablecisi.ailovebacked.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ConversationVO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:29
 **/
@Data
public class ConversationVO {
    private Long id;
    private Long userId;
    private Long characterId;
    private String characterName;     // 连表 ai_character
    private String characterAvatar;   // 连表 ai_character.image_url
    private String title;
    private String lastMessage;
    private LocalDateTime lastMsgAt;
    private LocalDateTime updateTime;
}
