package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.exception.ForbiddenException;
import com.ablecisi.ailovebacked.pojo.dto.PostCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.PostInteractionStateQueryDTO;
import com.ablecisi.ailovebacked.pojo.dto.PostLikeDTO;
import com.ablecisi.ailovebacked.pojo.dto.PostShareDTO;
import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.pojo.vo.PostInteractionStateVO;
import com.ablecisi.ailovebacked.pojo.vo.PostLikeResultVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.PostAppService;
import com.ablecisi.ailovebacked.service.PostFeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostFeedService postFeedService;
    private final PostAppService postAppService;

    @GetMapping("/feed")
    public Result<List<PostFeedVO>> feed(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return Result.success(postFeedService.pageFeed(page, size));
    }

    /**
     * 无需登录；点赞/关注态请登录后调 {@code /interaction-state} 合并
     */
    @GetMapping("/detail")
    public Result<PostFeedVO> detail(@RequestParam Long id) {
        return Result.success(postAppService.getDetail(id, null));
    }

    @PostMapping("/create")
    public Result<Map<String, Long>> create(@RequestBody @Valid PostCreateDTO dto) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new ForbiddenException("未登录");
        }
        long pid = postAppService.createPost(uid, dto);
        return Result.success(Map.of("id", pid));
    }

    @PostMapping("/like")
    public Result<PostLikeResultVO> like(@RequestBody @Valid PostLikeDTO dto) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new ForbiddenException("未登录");
        }
        return Result.success(postAppService.toggleLike(uid, dto));
    }

    @PostMapping("/share")
    public Result<Void> share(@RequestBody @Valid PostShareDTO dto) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new ForbiddenException("未登录");
        }
        postAppService.recordShare(dto.getPostId());
        return Result.success();
    }

    @PostMapping("/interaction-state")
    public Result<List<PostInteractionStateVO>> interactionState(
            @RequestBody @Valid PostInteractionStateQueryDTO dto) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new ForbiddenException("未登录");
        }
        return Result.success(postAppService.interactionStates(uid, dto.getPostIds()));
    }
}
