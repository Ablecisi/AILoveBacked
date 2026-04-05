package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostLikeDTO {
    @NotNull
    private Long postId;
    /**
     * true 点赞，false 取消
     */
    @NotNull
    private Boolean like;
}
