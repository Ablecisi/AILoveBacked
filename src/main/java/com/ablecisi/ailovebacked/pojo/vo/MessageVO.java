package com.ablecisi.ailovebacked.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * MessageVO
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
public class MessageVO {
    private Long id;
    private Integer type;           // 0用户/1AI
    private String content;
    private String emotion;         // ★
    private Double confidence;      // ★
    private Long conversationId;
    private Short isRead;
    private LocalDateTime createTime;
}
