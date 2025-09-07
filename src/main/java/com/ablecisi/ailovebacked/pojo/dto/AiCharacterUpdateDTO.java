package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
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
 * 14:07
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiCharacterUpdateDTO implements Serializable {
    @NotNull
    private Long id;
    private String name;
    private Long typeId;
    private Integer gender;
    private Integer age;
    private String imageUrl;
    private String traits;
    private String personaDesc;
    private String interests;
    private String backstory;
    private Integer online;   // 可单独改
    private Integer status;   // 可单独改
}
