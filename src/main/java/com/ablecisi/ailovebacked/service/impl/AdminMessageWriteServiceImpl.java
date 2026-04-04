package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.MessageMapper;
import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.service.AdminMessageWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMessageWriteServiceImpl implements AdminMessageWriteService {

    private final MessageMapper messageMapper;

    @Override
    public Message get(Long id) {
        Message m = messageMapper.selectEntityById(id);
        if (m == null) {
            throw new BaseException("消息不存在");
        }
        return m;
    }

    @Override
    @Transactional
    public long create(Message row) {
        if (row.getConversationId() == null || row.getUserId() == null || row.getType() == null) {
            throw new BaseException("conversationId、userId、type 不能为空");
        }
        if (row.getContent() == null) {
            throw new BaseException("content 不能为空");
        }
        if (row.getIsRead() == null) {
            row.setIsRead((short) 0);
        }
        messageMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public void update(Long id, Message row) {
        if (messageMapper.selectEntityById(id) == null) {
            throw new BaseException("消息不存在");
        }
        row.setId(id);
        messageMapper.updateRow(row);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (messageMapper.deleteById(id) == 0) {
            throw new BaseException("消息不存在");
        }
    }
}
