package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.MessageMapper;
import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * MessageService
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:42
 **/
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMapper messageMapper;

    /**
     * 保存用户发送的消息
     *
     * @param cid  会话 ID
     * @param uid  用户 ID
     * @param text 消息内容
     */
    public Message saveUserMessage(Long cid, Long uid, String text) {
        Message m = new Message();
        m.setConversationId(cid);
        m.setUserId(uid);
        m.setType(0); // 用户
        m.setContent(text);
        m.setIsRead((short) 1);
        messageMapper.insert(m);
        return m;
    }

    /**
     * 更新消息的的情绪信息
     * @param msgId 消息 ID
     * @param e 的情绪信息
     */
    public void updateEmotion(Long msgId, EmotionClient.EmotionDTO e) {
        messageMapper.updateEmotion(msgId, e.getEmotion(), e.getConfidence());
    }

    /**
     * 保存 AI 发送的消息
     * @param cid 会话 ID
     * @param uid 用户 ID
     * @param text 消息内容
     */
    public Message saveAiMessage(Long cid, Long uid, String text) {
        Message m = new Message();
        m.setConversationId(cid);
        m.setUserId(uid); // 可为 null；若要记录同 userId 也可以
        m.setType(1); // AI
        m.setContent(text);
        m.setIsRead((short) 0);
        messageMapper.insert(m);
        return m;
    }

    /**
     * 取最近 N 条并拼成“历史上下文”
     * @param cid 会话 ID
     * @param n 最近 N 条
     */
    public String briefContext(Long cid, int n) {
        List<MessageVO> latest = messageMapper.latestN(cid, 2 * n);
        if (latest == null || latest.isEmpty()) return "";
        // latest 是倒序，翻为正序
        latest.sort(Comparator.comparing(MessageVO::getId));
        StringBuilder sb = new StringBuilder();
        for (MessageVO m : latest) {
            sb.append(m.getType() != null && m.getType() == 0 ? "用户" : "AI")
                    .append("：").append(m.getContent()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * 分页查询会话中的消息
     * @param conversationId 会话 ID
     * @param page 页码
     * @param size 每页数量
     * @param offset 偏移量
     */
    public List<MessageVO> pageByConversation(Long conversationId, int page, int size, int offset) {
        return messageMapper.pageByConversation(conversationId, page, size, offset);
    }
}

