package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.exception.ForbiddenException;
import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.mapper.ConversationMapper;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.entity.Conversation;
import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * DialogService
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:46
 **/
@Service
@RequiredArgsConstructor
public class DialogService {

    private final EmotionClient emotionClient;
    private final UserApiRateLimiter userApiRateLimiter;
    private final PromptService promptService;
    private final LlmClient llmClient;
    private final MessageService messageService;
    private final ProfileService profileService;
    private final AiCharacterMapper aiCharacterMapper;
    private final ConversationMapper conversationMapper;

    private void assertUserOwnsConversation(Long conversationId, Long userId) {
        Conversation c = conversationMapper.selectById(conversationId);
        if (c == null) {
            throw new BaseException("会话不存在");
        }
        if (!c.getUserId().equals(userId)) {
            throw new ForbiddenException("无权访问该会话");
        }
    }

    private void assertCharacterMatchesConversation(Long conversationId, Long characterId) {
        Conversation c = conversationMapper.selectById(conversationId);
        if (c == null) {
            throw new BaseException("会话不存在");
        }
        if (!c.getCharacterId().equals(characterId)) {
            throw new BaseException("角色与会话不匹配");
        }
    }

    @Transactional
    public ChatReplyVO handleUserMessage(ChatSendDTO dto) {
        userApiRateLimiter.assertChatSendAllowed(dto.getUserId());
        assertUserOwnsConversation(dto.getConversationId(), dto.getUserId());
        assertCharacterMatchesConversation(dto.getConversationId(), dto.getCharacterId());
        // 1. 保存用户消息
        Message um = messageService.saveUserMessage(dto.getConversationId(), dto.getUserId(), dto.getText());

        // 2. 情绪识别 & 回填
        var emo = emotionClient.detect(dto.getText());
        messageService.updateEmotion(um.getId(), emo);

        // 3. 更新画像 & 获取摘要
        String profileBrief = profileService.updateAndSummarize(dto.getUserId(), emo.getEmotion());

        // 4. 历史上下文（最近若干条）
        String lastDialogue = messageService.briefContext(dto.getConversationId(), 8);

        // 5. Prompt 渲染（角色 + 画像 + 情绪 + 历史 + 用户文本）
        String prompt = promptService.renderWithCharacter(dto.getCharacterId(), emo, profileBrief, lastDialogue, dto.getText());

        // 6. 调 LLM（非流式；如需流式改用 generateStream）
        String reply = llmClient.generate(prompt);

        // 7. 保存AI消息
        Message am = messageService.saveAiMessage(dto.getConversationId(), dto.getUserId(), reply);

        // 8. 更新会话摘要/时间
        String brief = reply.length() > 180 ? reply.substring(0, 180) : reply;
        conversationMapper.updateLast(dto.getConversationId(), brief, LocalDateTime.now());

        // 9. 返回
        AiCharacterVO role = aiCharacterMapper.selectById(dto.getCharacterId());
        return new ChatReplyVO(
                reply, emo.getEmotion(), emo.getConfidence(),
                role == null ? null : role.getTypeName(),
                dto.getCharacterId(),
                am.getId(),
                llmClient.tokensUsed()
        );
    }

    /**
     * 流式版本：Consumer<String> onDelta 里推给前端（SSE / WebSocket 均可）
     */
    @Transactional
    public ChatReplyVO handleUserMessageStream(ChatSendDTO dto, Consumer<String> onDelta) throws IOException {
        userApiRateLimiter.assertChatSendAllowed(dto.getUserId());
        assertUserOwnsConversation(dto.getConversationId(), dto.getUserId());
        assertCharacterMatchesConversation(dto.getConversationId(), dto.getCharacterId());
        Message um = messageService.saveUserMessage(dto.getConversationId(), dto.getUserId(), dto.getText());
        var emo = emotionClient.detect(dto.getText());
        messageService.updateEmotion(um.getId(), emo);
        String profileBrief = profileService.updateAndSummarize(dto.getUserId(), emo.getEmotion());
        String lastDialogue = messageService.briefContext(dto.getConversationId(), 8);
        String prompt = promptService.renderWithCharacter(dto.getCharacterId(), emo, profileBrief, lastDialogue, dto.getText());

        String reply = llmClient.generateStream(prompt, onDelta);

        Message am = messageService.saveAiMessage(dto.getConversationId(), dto.getUserId(), reply);
        String brief = reply.length() > 180 ? reply.substring(0, 180) : reply;
        conversationMapper.updateLast(dto.getConversationId(), brief, LocalDateTime.now());

        AiCharacterVO role = aiCharacterMapper.selectById(dto.getCharacterId());
        return new ChatReplyVO(
                reply, emo.getEmotion(), emo.getConfidence(),
                role == null ? null : role.getTypeName(),
                dto.getCharacterId(),
                am.getId(),
                llmClient.tokensUsed()
        );
    }

    public List<MessageVO> listMessages(Long conversationId, int page, int size, Long currentUserId) {
        assertUserOwnsConversation(conversationId, currentUserId);
        int offset = (page - 1) * size;
        List<MessageVO> ml = messageService.pageByConversation(conversationId, page, size, offset);
        // 倒序改正序
        ml.sort(Comparator.comparing(MessageVO::getCreateTime));
        return ml;
    }
}
