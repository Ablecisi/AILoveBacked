package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminMessageListVO {
    private Long id;
    private Long conversationId;
    private Long userId;
    private Integer type;
    private String contentPreview;
    private String emotion;
    private Double confidence;
    private LocalDateTime createTime;
}
