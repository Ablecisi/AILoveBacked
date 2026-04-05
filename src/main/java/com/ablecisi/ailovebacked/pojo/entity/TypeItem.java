package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeItem {
    private Long id;
    private String name;
    /**
     * 对话语气/风格，写入系统提示 {style}；空则服务端用全局默认
     */
    private String promptStyle;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
