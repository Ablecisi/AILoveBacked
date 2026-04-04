package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionLog {
    private Long id;
    private Long userId;
    private Long messageId;
    private String emotion;
    private BigDecimal confidence;
    private LocalDateTime createTime;
}
