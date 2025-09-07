package com.ablecisi.ailovebacked.pojo.dto;

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
 * 14:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiCharacterQueryDTO implements Serializable {
    private Long userId;      // 查用户自建
    private Long typeId;
    private Integer status;   // 1/0
    private String keyword;   // 按 name/desc/persona/backstory 模糊
    private Integer page = 1;
    private Integer size = 10;
    private Integer offset;  // 计算得出，前端无需传
    private String orderBy = "update_time"; // 可选：create_time / name 等
    private String order = "DESC";          // ASC/DESC
}
