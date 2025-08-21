package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 * ArticleVO 类，表示文章信息的视图对象 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 00:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleVO implements Serializable {
    private String id;
    private String title; // 文章标题
    private String description; // 文章描述
    private String content; // 文章内容，可能是HTML格式
    private String coverImageUrl; // 封面图片URL
    private String authorId; // 作者ID
    private String authorName; // 作者名称
    private String authorAvatarUrl; // 作者头像URL
    private LocalDateTime publishTime; // 发布时间，使用时间戳表示
    private Integer viewCount; // 浏览量 int--> 最大值为2,147,483,647
    private Integer likeCount; // 点赞数
    private Integer commentCount; // 评论数
    private Boolean isLiked; // 是否已点赞
    private Boolean isBookmarked; // 是否已收藏
    private List<String> tags; // 文章标签列表
    private List<CommentVO> comments; // 文章评论列表
}
