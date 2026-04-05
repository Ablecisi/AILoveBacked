package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import com.ablecisi.ailovebacked.pojo.vo.MessageVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.DialogService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

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
@Slf4j
public class DialogController {

    private final DialogService dialogService;
    private final Executor dialogSseExecutor;

    public DialogController(DialogService dialogService,
                            @Qualifier("dialogSseExecutor") Executor dialogSseExecutor) {
        this.dialogService = dialogService;
        this.dialogSseExecutor = dialogSseExecutor;
    }

    @PostMapping("/send")
    public Result<ChatReplyVO> send(@RequestBody @Valid ChatSendDTO dto) {
        log.info("用户 {} 发送消息到会话 {}: {}", BaseContext.getCurrentId(), dto.getConversationId(), dto.getText());
        dto.setUserId(BaseContext.getCurrentId()); // JWT 注入
        return Result.success(dialogService.handleUserMessage(dto));
    }

    /**
     * SSE 流式对话：事件 {@code chunk} 为 JSON 字符串片段；{@code done} 为完整 {@link ChatReplyVO}；{@code error} 为错误文案。
     * 客户端需 {@code Accept: text/event-stream}，使用可流式读取的 HTTP 客户端（如 OkHttp）。
     */
    @PostMapping(value = "/send/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendStream(@RequestBody @Valid ChatSendDTO dto) {
        log.info("用户 {} 流式发送消息到会话 {}: {}", BaseContext.getCurrentId(), dto.getConversationId(), dto.getText());
        dto.setUserId(BaseContext.getCurrentId());
        SseEmitter emitter = new SseEmitter(0L);
        dialogSseExecutor.execute(() -> {
            try {
                ChatReplyVO vo = dialogService.handleUserMessageStream(dto, piece -> {
                    try {
                        // APPLICATION_JSON：data 行为合法 JSON，避免正文换行破坏 SSE，移动端可用 JSON 解析为 String
                        emitter.send(SseEmitter.event().name("chunk").data(piece, MediaType.APPLICATION_JSON));
                    } catch (IOException ignored) {
                    }
                });
                emitter.send(SseEmitter.event().name("done").data(vo, MediaType.APPLICATION_JSON));
                emitter.complete();
            } catch (Exception e) {
                log.warn("流式对话失败", e);
                try {
                    String msg = e.getMessage() == null ? "流式对话失败" : e.getMessage();
                    emitter.send(SseEmitter.event().name("error").data(msg, MediaType.APPLICATION_JSON));
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
        return Result.success(dialogService.listMessages(conversationId, page, size, BaseContext.getCurrentId()));
    }
}

