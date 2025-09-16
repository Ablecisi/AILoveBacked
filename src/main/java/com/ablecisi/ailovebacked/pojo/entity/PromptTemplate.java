package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * PromptTemplate
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.entity <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromptTemplate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleType;   // 例如: companion/mentor/…
    private String name;
    private String template;   // 含占位符
    private Integer status;    // 1/0
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
