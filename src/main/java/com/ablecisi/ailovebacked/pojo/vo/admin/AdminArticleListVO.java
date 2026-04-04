package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminArticleListVO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
