package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.ConversationMapper;
import com.ablecisi.ailovebacked.pojo.entity.Conversation;
import com.ablecisi.ailovebacked.service.AdminConversationWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminConversationWriteServiceImpl implements AdminConversationWriteService {

    private final ConversationMapper conversationMapper;

    @Override
    public Conversation get(Long id) {
        Conversation c = conversationMapper.selectById(id);
        if (c == null) {
            throw new BaseException("会话不存在");
        }
        return c;
    }

    @Override
    @Transactional
    public long create(Conversation row) {
        if (row.getUserId() == null || row.getCharacterId() == null) {
            throw new BaseException("userId 与 characterId 不能为空");
        }
        conversationMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public void update(Long id, Conversation row) {
        if (conversationMapper.selectById(id) == null) {
            throw new BaseException("会话不存在");
        }
        row.setId(id);
        conversationMapper.updateRow(row);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (conversationMapper.deleteById(id) == 0) {
            throw new BaseException("会话不存在");
        }
    }
}
