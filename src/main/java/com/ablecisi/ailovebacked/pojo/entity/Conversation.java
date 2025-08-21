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
 * Conversation 实体类，表示用户之间的对话信息 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Conversation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long characterId; // 角色ID， 用于唯一标识聊天会话中的角色
    private Long userId; // 用户ID，表示当前会话的用户
    private String lastMessage; // 最后消息内容
    private LocalDateTime createTime; // 创建时间，使用 LocalDateTime 表示
    private LocalDateTime updateTime; // 更新时间，使用 LocalDateTime 表示
}
