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
public class PostLikeRelation {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
