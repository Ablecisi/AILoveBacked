package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChatReplyVO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatReplyVO {
    private String reply;
    private String emotion;
    private Double confidence;
    private String roleType; // 可选：返回角色类型名
    private Long messageId;  // AI消息ID
    private Integer tokensUsed; // 本次对话消耗的Token数
}
