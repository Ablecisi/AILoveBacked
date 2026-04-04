package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.EmotionLogMapper;
import com.ablecisi.ailovebacked.pojo.entity.EmotionLog;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminEmotionLogManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEmotionLogManageServiceImpl implements AdminEmotionLogManageService {

    private final EmotionLogMapper emotionLogMapper;

    @Override
    public PageResult<EmotionLog> page(int page, int size) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        long total = emotionLogMapper.countAll();
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<EmotionLog> records = emotionLogMapper.selectPage(offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public EmotionLog get(Long id) {
        EmotionLog e = emotionLogMapper.selectById(id);
        if (e == null) {
            throw new BaseException("记录不存在");
        }
        return e;
    }

    @Override
    @Transactional
    public long create(EmotionLog row) {
        if (row.getUserId() == null || row.getMessageId() == null || row.getEmotion() == null || row.getConfidence() == null) {
            throw new BaseException("userId、messageId、emotion、confidence 不能为空");
        }
        emotionLogMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public void update(Long id, EmotionLog row) {
        if (emotionLogMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        row.setId(id);
        emotionLogMapper.updateRow(row);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (emotionLogMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
    }
}
