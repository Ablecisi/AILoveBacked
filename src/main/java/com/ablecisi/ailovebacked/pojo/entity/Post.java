package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.entity <br>
 * 帖子实体类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:05
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId; // 用户ID
    private String content; // 帖子内容
    private List<String> imageUrls; // 帖子图片URL列表
    private List<String> tags; // 帖子标签列表
    private Integer commentCount; // 评论数
    private Integer viewCount; // 浏览量
    private Integer likeCount; // 点赞数
    private Integer shareCount; // 分享数
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
