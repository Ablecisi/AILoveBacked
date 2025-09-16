package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserProfile
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.entity <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String interests;      // JSON或逗号分隔
    private String tonePreference; // 偏好语气
    private String emotionStats;   // JSON统计
    private String toDo;           // 待办事项，JSON数组
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
