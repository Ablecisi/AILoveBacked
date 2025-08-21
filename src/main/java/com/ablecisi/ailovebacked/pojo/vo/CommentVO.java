package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 * CommentVO 类，表示用户评论视图对象 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 01:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentVO implements Serializable {
    private String id;
    private String content;
    private String userId;
    private String userName; // 用户名称
    private String userAvatarUrl; // 用户头像URL
    private Integer likeCount; // 点赞数
    private Boolean isLiked; // 是否已点赞
    private String parentId; // 父评论ID，如果是回复评论的话
    private List<CommentVO> replies; // 回复列表
    private Long createTime; // 创建时间，使用时间戳表示
}
