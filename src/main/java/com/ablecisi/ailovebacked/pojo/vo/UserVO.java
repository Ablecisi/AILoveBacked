package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.entity.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/13
 * 星期五
 * 23:25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO implements Serializable {
    private String id; // 用户ID
    private String username; // 用户名
    private String name; // 昵称
    private String description; // 用户简介
    private String avatarUrl; // 头像URL
    private Integer followingCount; // 关注人数
    private Integer followersCount; // 粉丝人数
    private List<Long> postIds; // 用户帖子ID列表
    private List<Long> characterIds; // 我的角色ID列表
    private Boolean isFollowed; // 是否已关注
    private String token = "";  // 用户令牌
}
