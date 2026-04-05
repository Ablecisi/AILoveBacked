package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.PostFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostFeedService postFeedService;

    /**
     * 获取帖子列表
     *
     * @param page 页码
     * @param size 每页数量
     * @return 帖子列表
     */
    @GetMapping("/feed")
    public Result<List<PostFeedVO>> feed(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return Result.success(postFeedService.pageFeed(page, size));
    }
}
