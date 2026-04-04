package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.Message;

public interface AdminMessageWriteService {
    Message get(Long id);

    long create(Message row);

    void update(Long id, Message row);

    void delete(Long id);
}
