package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.exception.ForbiddenException;
import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.mapper.ConversationMapper;
import com.ablecisi.ailovebacked.mapper.EmotionLogMapper;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.dto.OpenConversationDTO;
import com.ablecisi.ailovebacked.pojo.entity.Conversation;
import com.ablecisi.ailovebacked.pojo.entity.EmotionLog;
import com.ablecisi.ailovebacked.pojo.entity.Message;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import com.ablecisi.ailovebacked.pojo.vo.ConversationVO;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class DialogService {

    private final EmotionClient emotionClient;
    private final EmotionLogMapper emotionLogMapper;
    private final UserApiRateLimiter userApiRateLimiter;
    private final PromptService promptService;
    private final LlmClient llmClient;
    private final MessageService messageService;
    private final ProfileService profileService;
    private final AiCharacterMapper aiCharacterMapper;
    private final ConversationMapper conversationMapper;

    private Conversation requireOwnedConversation(Long conversationId, Long userId) {
        Conversation c = conversationMapper.selectById(conversationId);
        if (c == null) {
            throw new BaseException("会话不存在");
        }
        if (!c.getUserId().equals(userId)) {
            throw new ForbiddenException("无权访问该会话");
        }
        return c;
    }

    private static void assertCharacterMatches(Conversation c, Long characterId) {
        if (!c.getCharacterId().equals(characterId)) {
            throw new BaseException("角色与会话不匹配");
        }
    }

    @Transactional
    public ChatReplyVO handleUserMessage(ChatSendDTO dto) {
        userApiRateLimiter.assertChatSendAllowed(dto.getUserId());
        Conversation conv = requireOwnedConversation(dto.getConversationId(), dto.getUserId());
        assertCharacterMatches(conv, dto.getCharacterId());
        String sessionScene = conv.getSceneBackground();
        // 1. 保存用户消息
        Message um = messageService.saveUserMessage(dto.getConversationId(), dto.getUserId(), dto.getText());

        // 2. 情绪识别 & 回填
        var emo = emotionClient.detect(dto.getText());
        messageService.updateEmotion(um.getId(), emo);
        recordEmotionLog(dto.getUserId(), um.getId(), emo);

        // 3. 更新画像 & 获取摘要
        String profileBrief = profileService.updateAndSummarize(dto.getUserId(), emo.getEmotion());

        // 4. 历史上下文（最近若干条）
        String lastDialogue = messageService.briefContext(dto.getConversationId(), 8);

        // 5. Prompt 渲染（角色 + 画像 + 情绪 + 历史 + 用户文本）
        String prompt = promptService.renderWithCharacter(dto.getCharacterId(), emo, profileBrief, lastDialogue, dto.getText(), dto.getUserId(), sessionScene);

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
    public ChatReplyVO handleUserMessageStream(ChatSendDTO dto,
                                               Runnable onPreprocessDone,
                                               Consumer<String> onDelta) throws IOException {
        long tStart = System.currentTimeMillis();
        userApiRateLimiter.assertChatSendAllowed(dto.getUserId());
        Conversation conv = requireOwnedConversation(dto.getConversationId(), dto.getUserId());
        assertCharacterMatches(conv, dto.getCharacterId());
        String sessionScene = conv.getSceneBackground();
        Message um = messageService.saveUserMessage(dto.getConversationId(), dto.getUserId(), dto.getText());
        var emo = emotionClient.detect(dto.getText());
        messageService.updateEmotion(um.getId(), emo);
        recordEmotionLog(dto.getUserId(), um.getId(), emo);
        String profileBrief = profileService.updateAndSummarize(dto.getUserId(), emo.getEmotion());
        String lastDialogue = messageService.briefContext(dto.getConversationId(), 8);
        String prompt = promptService.renderWithCharacter(dto.getCharacterId(), emo, profileBrief, lastDialogue, dto.getText(), dto.getUserId(), sessionScene);
        long tPreDone = System.currentTimeMillis();
        if (onPreprocessDone != null) {
            onPreprocessDone.run();
        }

        final boolean[] firstDeltaSeen = {false};
        String reply = llmClient.generateStream(prompt, piece -> {
            if (!firstDeltaSeen[0]) {
                firstDeltaSeen[0] = true;
                log.info("stream stage first-delta userId={} convId={} preprocessMs={}",
                        dto.getUserId(), dto.getConversationId(), (tPreDone - tStart));
            }
            if (onDelta != null) {
                onDelta.accept(piece);
            }
        });

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
        requireOwnedConversation(conversationId, currentUserId);
        int offset = (page - 1) * size;
        List<MessageVO> ml = messageService.pageByConversation(conversationId, page, size, offset);
        // 倒序改正序
        ml.sort(Comparator.comparing(MessageVO::getCreateTime));
        return ml;
    }

    /**
     * 当前用户的会话列表（按最近消息时间倒序）
     */
    private void recordEmotionLog(Long userId, Long messageId, EmotionClient.EmotionDTO emo) {
        try {
            double c = emo.getConfidence() == null ? 0.0 : emo.getConfidence();
            EmotionLog row = EmotionLog.builder()
                    .userId(userId)
                    .messageId(messageId)
                    .emotion(emo.getEmotion())
                    .confidence(BigDecimal.valueOf(c))
                    .build();
            emotionLogMapper.insert(row);
        } catch (Exception e) {
            log.warn("写入 emotion_log 失败: {}", e.getMessage());
        }
    }

    public List<ConversationVO> listMyConversations(int page, int size, Long userId) {
        if (userId == null) {
            throw new ForbiddenException("未登录");
        }
        int offset = (page - 1) * size;
        return conversationMapper.pageByUser(userId, page, size, offset);
    }

    /**
     * 创建新会话：绑定角色与可选场景背景。
     */
    @Transactional
    public ConversationVO openConversation(Long userId, OpenConversationDTO dto) {
        if (userId == null) {
            throw new ForbiddenException("未登录");
        }
        AiCharacterVO ch = aiCharacterMapper.selectById(dto.getCharacterId());
        if (ch == null) {
            throw new BaseException("角色不存在");
        }
        if (ch.getUserId() != null && !ch.getUserId().equals(userId)) {
            throw new ForbiddenException("无权使用该角色");
        }
        if (ch.getStatus() != null && ch.getStatus() == 0) {
            throw new BaseException("角色已下线");
        }
        String title = dto.getTitle();
        if (title == null || title.isBlank()) {
            title = (ch.getName() != null && !ch.getName().isBlank())
                    ? ch.getName() + " 的对话"
                    : "新对话";
        }
        String scene = dto.getSceneBackground();
        if (scene != null) {
            scene = scene.trim();
            if (scene.isEmpty()) {
                scene = null;
            }
        }
        Conversation row = Conversation.builder()
                .userId(userId)
                .characterId(dto.getCharacterId())
                .title(title)
                .sceneBackground(scene)
                .lastMessage("")
                .lastMsgAt(LocalDateTime.now())
                .build();
        conversationMapper.insert(row);
        ConversationVO vo = conversationMapper.selectVoById(row.getId());
        if (vo == null) {
            throw new BaseException("创建会话失败");
        }
        return vo;
    }
}
