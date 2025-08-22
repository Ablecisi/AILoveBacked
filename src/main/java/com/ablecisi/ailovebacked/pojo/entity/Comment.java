package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.entity <br>
 * Comment 实体类，表示用户评论信息 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 18:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 评论ID
    private String content; // 评论内容
    private Long postId; // 评论所属的帖子ID
    private Long articleId; // 评论所属的文章ID
    private String userId; // 评论用户ID
    private Integer likeCount; // 点赞数
    private Integer replyCount; // 直接回复数
    private Long parentId; // 父评论ID，如果是回复评论的话
    private Long rootId; // 根评论ID，用于标识评论树的根节点
    private String path; // 评论路径，用于表示评论的层级关系
    private Integer depth; // 评论层级，表示评论的深度
    private Boolean isDeleted; // 是否删除
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
