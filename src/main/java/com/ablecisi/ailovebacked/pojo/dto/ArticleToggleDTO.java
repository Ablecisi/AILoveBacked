package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleToggleDTO {
    @NotNull
    private Long articleId;
    /**
     * true = 点赞/收藏，false = 取消
     */
    @NotNull
    private Boolean active;
}
