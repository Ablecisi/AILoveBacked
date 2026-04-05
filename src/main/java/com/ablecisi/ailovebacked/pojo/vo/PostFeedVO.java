package com.ablecisi.ailovebacked.pojo.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * App 社区帖子列表项（含作者摘要）
 */
@Data
@Builder
public class PostFeedVO {
    private String id;
    private String authorId;
    private String authorName;
    private String authorAvatarUrl;
    private String content;
    private List<String> imageUrls;
    private List<String> tags;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private LocalDateTime createdAt;
    private Boolean liked;
}
