package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/7
 * 星期日
 * 14:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiCharacterCreateDTO implements Serializable {
    @NotBlank
    private String name;
    private Long userId;      // 系统内置可为 null
    private Long typeId;
    private Integer gender;   // 默认 2
    private Integer age;
    private String imageUrl;
    private String traits;        // JSON 字符串或逗号分隔
    private String personaDesc;
    private String interests;     // JSON 字符串或逗号分隔
    private String backstory;
    private Integer status;       // 默认 1
}
