package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminConversationListVO {
    private Long id;
    private Long userId;
    private Long characterId;
    private String characterName;
    private String title;
    private String lastMessage;
    private LocalDateTime lastMsgAt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
