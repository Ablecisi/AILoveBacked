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
public class FollowRelation {
    private Long id;
    private Long followingId;
    private Long followerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
