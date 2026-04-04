package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.ConversationMapper;
import com.ablecisi.ailovebacked.mapper.MessageMapper;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminConversationListVO;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminMessageListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminDialogManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDialogManageServiceImpl implements AdminDialogManageService {

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;

    @Override
    public PageResult<AdminConversationListVO> pageConversations(int page, int size, String keyword, Long userId) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = conversationMapper.countPageForAdmin(kw, userId);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<AdminConversationListVO> records = conversationMapper.pageForAdmin(kw, userId, offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public PageResult<AdminMessageListVO> pageMessages(int page, int size, String keyword, Long conversationId) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = messageMapper.countPageForAdmin(kw, conversationId);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<AdminMessageListVO> records = messageMapper.pageForAdmin(kw, conversationId, offset, s);
        return new PageResult<>(total, records);
    }
}
