package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.pojo.vo.ArticleVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ArticleController
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.controller <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 22:57
 **/
@RestController
@RequestMapping("/api/article")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 根据文章ID获取文章详情
     *
     * @param articleId 文章ID
     * @return 文章详情
     */
    @GetMapping("/byId")
    public Result<ArticleVO> getArticleById(String articleId) {
        log.info("请求id为 {} 的文章详情", articleId);
        Long id = parseId(articleId);
        ArticleVO article = articleService.getArticleById(id);
        return Result.success(article);
    }

    /**
     * 根据文章ID获取相关文章列表
     *
     * @param articleId 文章ID
     * @return 相关文章列表
     */
    @GetMapping("/related")
    public Result<List<ArticleVO>> getRelatedArticles(String articleId) {
        log.info("请求id为 {} 的相关文章列表", articleId);
        Long id = parseId(articleId);
        List<ArticleVO> relatedArticles = articleService.getRelatedArticles(id);
        return Result.success(relatedArticles);
    }

    /**
     * 获取热门文章列表
     *
     * @return 热门文章列表
     */
    @GetMapping("/hot")
    public Result<List<ArticleVO>> getHotArticles() {
        log.info("请求热门文章列表");
        List<ArticleVO> hotArticles = articleService.getHotArticles();
        return Result.success(hotArticles);
    }

    /**
     * 获取推荐文章
     *
     * @param tags 用户感兴趣的标签列表
     * @return 推荐文章
     */
    @PostMapping("/featured")
    public Result<ArticleVO> featureArticle(@RequestBody List<String> tags) {
        log.info("请求特色文章，标签: {}", tags);
        ArticleVO articleVO = articleService.getFeaturedArticle(tags);
        return Result.success(articleVO);
    }

    /**
     * 解析字符串形式的ID为Long类型
     *
     * @param idStr 字符串形式的ID
     * @return Long类型的ID
     * @throws BaseException 如果ID格式错误
     */
    private Long parseId(String idStr) {
        try {
            return Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            log.error("ID格式错误: {}", idStr, e);
            throw new BaseException("ID格式错误");
        }
    }
}
