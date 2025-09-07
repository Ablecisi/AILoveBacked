package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.pojo.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/7
 * 星期日
 * 14:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiCharacterVO {
    private Long id;
    private Long userId;
    private String name;
    private Long typeId;
    private String typeName;
    private Integer gender;
    private Integer age;
    private String imageUrl;
    private String traits;
    private String personaDesc;
    private String interests;
    private String backstory;
    private Integer online;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
