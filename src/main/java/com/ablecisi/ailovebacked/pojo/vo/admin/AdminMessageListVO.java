package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminMessageListVO {
    private Long id;
    private Long conversationId;
    /**
     * 会话标题（列表展示）
     */
    private String conversationTitle;
    private Long userId;
    /**
     * 用户昵称或登录名
     */
    private String userDisplayName;
    private Integer type;
    private String content;
    private String emotion;
    private Double confidence;
    private Integer isRead;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
