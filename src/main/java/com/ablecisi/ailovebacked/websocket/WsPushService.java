package com.ablecisi.ailovebacked.websocket;

import com.ablecisi.ailovebacked.pojo.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * AILoveBacked <br>
 * com.ailianlian.server.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/16
 * 星期六
 * 23:28
 **/
@Service
@RequiredArgsConstructor // 使用 Lombok 的 @RequiredArgsConstructor 自动生成构造函数
public class WsPushService {
    private final SimpMessagingTemplate template;

    /**
     * 推送消息到指定的会话
     *
     * @param conversationId 会话ID
     * @param vo             消息内容
     */
    // 注意：这里的会话ID是指 WebSocket 中的会话标识
    public void pushToConversation(Long conversationId, Message vo) {
        template.convertAndSend("/topic/conversations/" + conversationId, vo);// 推送到指定会话
    }
}

