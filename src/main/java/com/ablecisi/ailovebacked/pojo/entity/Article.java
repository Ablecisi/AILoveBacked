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
 * Article 实体类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 18:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 文章ID，主键
    private String title; // 文章标题
    private String description; // 文章描述
    private String content; // 文章内容，可能是HTML格式
    private String coverImageUrl; // 封面图片URL
    private Long userId; // 作者ID
    private Integer viewCount; // 浏览量 int--> 最大值为2,147,483,647
    private Integer likeCount; // 点赞数 热度计算： 点赞数 * 5 + 评论数 * 3 + 浏览量 * 1
    private Integer commentCount; // 评论数
    private List<String> tags; // 文章标签列表
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
