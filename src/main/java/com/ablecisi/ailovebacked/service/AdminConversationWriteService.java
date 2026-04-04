package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.Conversation;

public interface AdminConversationWriteService {
    Conversation get(Long id);

    long create(Conversation row);

    void update(Long id, Conversation row);

    void delete(Long id);
}
