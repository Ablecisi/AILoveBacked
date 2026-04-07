package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.pojo.dto.ChatSendDTO;
import com.ablecisi.ailovebacked.pojo.dto.OpenConversationDTO;
import com.ablecisi.ailovebacked.pojo.vo.ChatReplyVO;
import com.ablecisi.ailovebacked.pojo.vo.ConversationVO;
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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    /**
     * 发送对话消息
     *
     * @param dto 发送对话消息
     * @return 响应
     */
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
        String requestId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(0L);
        ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();
        Runnable stopHeartbeat = () -> {
            heartbeat.shutdownNow();
            try {
                heartbeat.awaitTermination(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        };
        emitter.onCompletion(stopHeartbeat);
        emitter.onTimeout(stopHeartbeat);
        emitter.onError(err -> stopHeartbeat.run());
        try {
            emitter.send(SseEmitter.event().name("ack")
                    .data(Map.of("requestId", requestId, "serverTime", System.currentTimeMillis()), MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            log.warn("stream ack send failed requestId={}", requestId, e);
            stopHeartbeat.run();
            emitter.complete();
            return emitter;
        }
        heartbeat.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat")
                        .data(Map.of("requestId", requestId, "ts", System.currentTimeMillis()), MediaType.APPLICATION_JSON));
            } catch (IOException ignored) {
                stopHeartbeat.run();
            }
        }, 12, 12, TimeUnit.SECONDS);
        dialogSseExecutor.execute(() -> {
            try {
                emitter.send(SseEmitter.event().name("start")
                        .data(Map.of("requestId", requestId), MediaType.APPLICATION_JSON));
                ChatReplyVO vo = dialogService.handleUserMessageStream(dto, () -> {
                    try {
                        emitter.send(SseEmitter.event().name("preprocess_done")
                                .data(Map.of("requestId", requestId, "ts", System.currentTimeMillis()), MediaType.APPLICATION_JSON));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, piece -> {
                    try {
                        // APPLICATION_JSON：data 行为合法 JSON，避免正文换行破坏 SSE，移动端可用 JSON 解析为 String
                        emitter.send(SseEmitter.event().name("chunk").data(piece, MediaType.APPLICATION_JSON));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                emitter.send(SseEmitter.event().name("done").data(vo, MediaType.APPLICATION_JSON));
                stopHeartbeat.run();
                emitter.complete();
            } catch (Exception e) {
                log.warn("流式对话失败 requestId={}", requestId, e);
                try {
                    String msg = e.getMessage() == null ? "流式对话失败" : e.getMessage();
                    if (msg.startsWith("HTTP 429")) {
                        msg = "AI服务当前繁忙(429)，请稍后重试";
                    }
                    emitter.send(SseEmitter.event().name("error").data(msg, MediaType.APPLICATION_JSON));
                } catch (IOException ignored) {
                }
                stopHeartbeat.run();
                // SSE 场景只发送 error 事件并正常结束，避免再次进入全局异常处理导致 converter 报错
                emitter.complete();
            }
        });
        return emitter;
    }

    /**
     * 拉取对话消息列表
     * @param conversationId 会话 ID
     * @param page 页码
     * @param size 页大小
     * @return 响应
     */
    @GetMapping("/list")
    public Result<List<MessageVO>> list(@RequestParam Long conversationId,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("分页拉取会话 {} 消息列表: page={}, size={}", conversationId, page, size);
        return Result.success(dialogService.listMessages(conversationId, page, size, BaseContext.getCurrentId()));
    }

    /**
     * 拉取我的会话列表
     *
     * @param page 页码
     * @param size 页大小
     * @return 响应
     */
    @GetMapping("/conversations")
    public Result<List<ConversationVO>> myConversations(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "50") int size) {
        return Result.success(dialogService.listMyConversations(page, size, BaseContext.getCurrentId()));
    }

    /**
     * 创建会话：选择角色并可选填写标题、场景背景。
     */
    @PostMapping("/conversation/open")
    public Result<ConversationVO> openConversation(@RequestBody @Valid OpenConversationDTO dto) {
        log.info("用户 {} 创建会话 characterId={}", BaseContext.getCurrentId(), dto.getCharacterId());
        return Result.success(dialogService.openConversation(BaseContext.getCurrentId(), dto));
    }
}

