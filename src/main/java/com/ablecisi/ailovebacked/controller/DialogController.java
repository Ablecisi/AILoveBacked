package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.DialogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * DialogController
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.controller <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:47
 **/
@RestController
@RequestMapping("/api/dialog")
@RequiredArgsConstructor
@Slf4j
public class DialogController {

    private final DialogService dialogService;

    @PostMapping("/send")
    public Result<ChatReplyVO> send(@RequestBody @Valid ChatSendDTO dto) {
        log.info("用户 {} 发送消息到会话 {}: {}", BaseContext.getCurrentId(), dto.getConversationId(), dto.getText());
        dto.setUserId(BaseContext.getCurrentId()); // JWT 注入
        return Result.success(dialogService.handleUserMessage(dto));
    }

    /**
     * SSE 流式；Android 也可长连接读取 chunk（OkHttp）。
     */
    @PostMapping(value = "/send/stream", produces = "text/event-stream")
    public SseEmitter sendStream(@RequestBody @Valid ChatSendDTO dto) {
        log.info("用户 {} 发送消息到会话 {}: {}", BaseContext.getCurrentId(), dto.getConversationId(), dto.getText());
        dto.setUserId(BaseContext.getCurrentId());
        SseEmitter emitter = new SseEmitter(0L);
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                ChatReplyVO vo = dialogService.handleUserMessageStream(dto, piece -> {
                    try {
                        emitter.send(SseEmitter.event().name("chunk").data(piece));
                    } catch (IOException ignored) {
                    }
                });
                emitter.send(SseEmitter.event().name("done").data(vo.getMessageId()));
                emitter.complete();
            } catch (Exception e) {
                try {
                    emitter.send(SseEmitter.event().name("error").data(e.getMessage()));
                } catch (IOException ignored) {
                }
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @GetMapping("/list")
    public Result<List<MessageVO>> list(@RequestParam Long conversationId,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("分页拉取会话 {} 消息列表: page={}, size={}", conversationId, page, size);
        return Result.success(dialogService.listMessages(conversationId, page, size));
    }
}

