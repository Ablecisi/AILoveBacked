package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminArticleListVO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String coverImageUrl;
    private Long userId;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private List<String> tags;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
