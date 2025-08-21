package com.ablecisi.ailovebacked.constant;

import lombok.Getter;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.constant <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 12:26
 **/
@Getter
public enum FollowMessageConstant {

    FOLLOW_SUCCESS("关注成功"),
    UNFOLLOW_SUCCESS("取消关注成功"),
    FOLLOW_ALREADY_EXISTS("已经关注过该用户"),
    UNFOLLOW_ALREADY_EXISTS("已经取消关注过该用户"),
    FOLLOW_ERROR("关注失败，请稍后再试"),
    UNFOLLOW_ERROR("取消关注失败，请稍后再试"),
    USER_NOT_FOUND("用户不存在"),
    AUTHOR_NOT_FOUND("作者不存在"),
    INVALID_USER_ID("无效的用户ID"),
    INVALID_AUTHOR_ID("无效的作者ID");

    private final String message;

    FollowMessageConstant(String message) {
        this.message = message;
    }
}
