package com.ablecisi.ailovebacked.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/28
 * 星期四
 * 23:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDTO implements Serializable {
    private String targetType;  // "article" | "post"
    private Long targetId;      // articleId 或 postId
    private Long parentId;      // 可空：顶层为 null
    private String content;     // 评论内容
}
