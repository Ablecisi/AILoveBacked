package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/29
 * 星期五
 * 00:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDTO implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    private String content;
}
