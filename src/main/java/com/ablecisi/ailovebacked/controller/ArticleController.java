package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.controller <br>
 * 文章控制器
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:23
 **/
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 获取用户精选文章
     *
     * @param userId 用户ID
     * @return 用户精选文章
     */
    @PostMapping("/featured")
    public Result<ArticleVO> getFeaturedArticles(@RequestBody String userId) {
        log.info("获取用户 {} 的精选文章", userId);
        if (userId == null || userId.isEmpty()) {
            log.warn("用户ID不能为空");
            return Result.error("用户ID不能为空");
        }
        ArticleVO articleVO = articleService.getFeaturedArticles(userId);
        if (articleVO == null) {
            log.warn("用户 {} 的精选文章未找到", userId);
            return Result.error("未找到精选文章");
        }
        log.info("成功获取用户 {} 的精选文章", userId);
        log.warn("获取到ID为 {} 的精选文章", articleVO.getId());
        return Result.success(articleVO);
    }

    /**
     * 根据文章ID获取文章
     *
     * @param id 文章ID
     * @return 文章视图对象
     */
    @GetMapping("/byId")
    public Result<ArticleVO> getArticleById(Long id) {
        log.info("获取文章ID为 {} 的文章", id);
        if (id == null) {
            log.warn("文章ID不能为空");
            return Result.error("文章ID不能为空");
        }
        ArticleVO articleVO = articleService.getArticleById(id);
        if (articleVO == null) {
            log.warn("文章ID为 {} 的文章未找到", id);
            return Result.error("未找到文章");
        }
        log.info("成功获取文章ID为 {} 的文章", id);
        return Result.success(articleVO);
    }

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    @GetMapping("/hot")
    public Result<List<ArticleVO>> getHotArticles() {
        log.info("获取热门文章列表");
        List<ArticleVO> hotArticles = articleService.getHotArticles();
        if (hotArticles == null || hotArticles.isEmpty()) {
            log.warn("没有找到热门文章");
            return Result.error("没有找到热门文章");
        }
        log.info("成功获取热门文章列表");
        return Result.success(hotArticles);
    }


}
