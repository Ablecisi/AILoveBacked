package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.entity <br>
 * Message 实体类，表示用户消息信息 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 18:57
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 消息ID
    private String content; // 消息内容
    private Integer type; // 0: 发送, 1: 接收
    private Short isRead; // 是否已读 0: 未读, 1: 已读
    private Long userId; // 用户ID，表示消息所属的用户
    private Long conversationId; // 会话ID，表示消息所属的会话
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
