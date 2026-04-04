package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.admin.AdminConversationListVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminMessageListVO;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminDialogManageService {
    PageResult<AdminConversationListVO> pageConversations(int page, int size, String keyword, Long userId);

    PageResult<AdminMessageListVO> pageMessages(int page, int size, String keyword, Long conversationId);
}
