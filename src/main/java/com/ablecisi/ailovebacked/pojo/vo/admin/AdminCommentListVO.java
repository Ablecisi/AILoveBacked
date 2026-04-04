package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCommentListVO {
    private Long id;
    private String content;
    private Long articleId;
    private Long postId;
    private Long userId;
    private String userName;
    private Long parentId;
    private Integer likeCount;
    private Integer replyCount;
    private Boolean deleted;
    private LocalDateTime createTime;
}
