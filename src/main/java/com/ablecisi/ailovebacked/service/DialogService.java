package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.mapper.ConversationMapper;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
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
    private final PromptService promptService;
    private final LlmClient llmClient;
    private final MessageService messageService;
    private final ProfileService profileService;
    private final AiCharacterMapper aiCharacterMapper;
    private final ConversationMapper conversationMapper;

    @Transactional
    public ChatReplyVO handleUserMessage(ChatSendDTO dto) {
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
                am.getId(),
                llmClient.tokensUsed()
        );
    }

    /**
     * 流式版本：Consumer<String> onDelta 里推给前端（SSE / WebSocket 均可）
     */
    @Transactional
    public ChatReplyVO handleUserMessageStream(ChatSendDTO dto, Consumer<String> onDelta) throws IOException {
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
                am.getId(),
                llmClient.tokensUsed()
        );
    }
}
