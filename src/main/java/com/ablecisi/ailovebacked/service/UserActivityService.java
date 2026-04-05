package com.ablecisi.ailovebacked.service;

/**
 * 记录 C 端用户当日是否活跃（用于 DAU）
 */
public interface UserActivityService {

    void touchCurrentUser(long userId);
}
