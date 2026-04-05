package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/29
 * 星期五
 * 00:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO implements Serializable {
    private Long id;
    private String content;
    private Long userId;
    private String userName;    // 冗余: 前端直接显示昵称
    private String avatarUrl;   // 冗余: 前端直接显示头像

    private Long parentId;
    /**
     * 父评论作者昵称（用于「A→B」展示；回复根评论时为空）
     */
    private String parentUserName;
    private Long rootId;
    private Integer depth;
    private Integer likeCount;
    /**
     * 当前登录用户是否已点赞该条评论（未登录为 false）
     */
    private Boolean liked;
    private Integer replyCount;
    /**
     * 该根评论下全部子孙条数（不含根自身）；仅顶层列表查询填充，用于客户端「展开 N 条回复」。
     */
    private Integer descendantCount;

    private Boolean deleted;    // 用 Boolean 替代 is_deleted，语义更清晰
    private LocalDateTime createTime;

    // ★ 新增：仅做增量分页游标使用
    private String pathCursor;
}
