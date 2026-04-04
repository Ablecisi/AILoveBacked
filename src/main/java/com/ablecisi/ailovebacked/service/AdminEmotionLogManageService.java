package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.EmotionLog;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminEmotionLogManageService {
    PageResult<EmotionLog> page(int page, int size);

    EmotionLog get(Long id);

    long create(EmotionLog row);

    void update(Long id, EmotionLog row);

    void delete(Long id);
}
