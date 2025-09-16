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

    public void updateEmotion(Long msgId, EmotionClient.EmotionDTO e) {
        messageMapper.updateEmotion(msgId, e.getEmotion(), e.getConfidence());
    }

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
}

