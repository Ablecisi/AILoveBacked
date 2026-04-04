package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.ArticleCollectAdminMapper;
import com.ablecisi.ailovebacked.mapper.ArticleLikeAdminMapper;
import com.ablecisi.ailovebacked.mapper.CommentLikeAdminMapper;
import com.ablecisi.ailovebacked.mapper.PostLikeAdminMapper;
import com.ablecisi.ailovebacked.pojo.entity.ArticleCollectRelation;
import com.ablecisi.ailovebacked.pojo.entity.ArticleLikeRelation;
import com.ablecisi.ailovebacked.pojo.entity.CommentLikeRelation;
import com.ablecisi.ailovebacked.pojo.entity.PostLikeRelation;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/admin/api/v1/relations")
@RequiredArgsConstructor
public class AdminRelationTablesController {

    private final ArticleLikeAdminMapper articleLikeAdminMapper;
    private final ArticleCollectAdminMapper articleCollectAdminMapper;
    private final CommentLikeAdminMapper commentLikeAdminMapper;
    private final PostLikeAdminMapper postLikeAdminMapper;

    private static int p(int page) {
        return Math.max(page, 1);
    }

    private static int s(int size) {
        return Math.min(Math.max(size, 1), 100);
    }

    // --- article like ---
    @GetMapping("/article-likes")
    public Result<PageResult<ArticleLikeRelation>> articleLikesPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int pi = p(page);
        int si = s(size);
        long total = articleLikeAdminMapper.countAll();
        if (total == 0) {
            return Result.success(new PageResult<>(0, Collections.emptyList()));
        }
        return Result.success(new PageResult<>(total, articleLikeAdminMapper.selectPage((pi - 1) * si, si)));
    }

    @GetMapping("/article-likes/{id}")
    public Result<ArticleLikeRelation> articleLikeGet(@PathVariable long id) {
        ArticleLikeRelation r = articleLikeAdminMapper.selectById(id);
        if (r == null) {
            throw new BaseException("记录不存在");
        }
        return Result.success(r);
    }

    @PostMapping("/article-likes")
    public Result<Map<String, Long>> articleLikeCreate(@RequestBody ArticleLikeRelation body) {
        articleLikeAdminMapper.insert(body);
        if (body.getId() == null) {
            throw new BaseException("创建失败");
        }
        return Result.success(Map.of("id", body.getId()));
    }

    @PutMapping("/article-likes/{id}")
    public Result<Void> articleLikeUpdate(@PathVariable long id, @RequestBody ArticleLikeRelation body) {
        if (articleLikeAdminMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        body.setId(id);
        articleLikeAdminMapper.updateRow(body);
        return Result.success();
    }

    @DeleteMapping("/article-likes/{id}")
    public Result<Void> articleLikeDelete(@PathVariable long id) {
        if (articleLikeAdminMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
        return Result.success();
    }

    // --- article collect ---
    @GetMapping("/article-collects")
    public Result<PageResult<ArticleCollectRelation>> articleCollectsPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int pi = p(page);
        int si = s(size);
        long total = articleCollectAdminMapper.countAll();
        if (total == 0) {
            return Result.success(new PageResult<>(0, Collections.emptyList()));
        }
        return Result.success(new PageResult<>(total, articleCollectAdminMapper.selectPage((pi - 1) * si, si)));
    }

    @GetMapping("/article-collects/{id}")
    public Result<ArticleCollectRelation> articleCollectGet(@PathVariable long id) {
        ArticleCollectRelation r = articleCollectAdminMapper.selectById(id);
        if (r == null) {
            throw new BaseException("记录不存在");
        }
        return Result.success(r);
    }

    @PostMapping("/article-collects")
    public Result<Map<String, Long>> articleCollectCreate(@RequestBody ArticleCollectRelation body) {
        articleCollectAdminMapper.insert(body);
        if (body.getId() == null) {
            throw new BaseException("创建失败");
        }
        return Result.success(Map.of("id", body.getId()));
    }

    @PutMapping("/article-collects/{id}")
    public Result<Void> articleCollectUpdate(@PathVariable long id, @RequestBody ArticleCollectRelation body) {
        if (articleCollectAdminMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        body.setId(id);
        articleCollectAdminMapper.updateRow(body);
        return Result.success();
    }

    @DeleteMapping("/article-collects/{id}")
    public Result<Void> articleCollectDelete(@PathVariable long id) {
        if (articleCollectAdminMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
        return Result.success();
    }

    // --- comment like ---
    @GetMapping("/comment-likes")
    public Result<PageResult<CommentLikeRelation>> commentLikesPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int pi = p(page);
        int si = s(size);
        long total = commentLikeAdminMapper.countAll();
        if (total == 0) {
            return Result.success(new PageResult<>(0, Collections.emptyList()));
        }
        return Result.success(new PageResult<>(total, commentLikeAdminMapper.selectPage((pi - 1) * si, si)));
    }

    @GetMapping("/comment-likes/{id}")
    public Result<CommentLikeRelation> commentLikeGet(@PathVariable long id) {
        CommentLikeRelation r = commentLikeAdminMapper.selectById(id);
        if (r == null) {
            throw new BaseException("记录不存在");
        }
        return Result.success(r);
    }

    @PostMapping("/comment-likes")
    public Result<Map<String, Long>> commentLikeCreate(@RequestBody CommentLikeRelation body) {
        commentLikeAdminMapper.insert(body);
        if (body.getId() == null) {
            throw new BaseException("创建失败");
        }
        return Result.success(Map.of("id", body.getId()));
    }

    @PutMapping("/comment-likes/{id}")
    public Result<Void> commentLikeUpdate(@PathVariable long id, @RequestBody CommentLikeRelation body) {
        if (commentLikeAdminMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        body.setId(id);
        commentLikeAdminMapper.updateRow(body);
        return Result.success();
    }

    @DeleteMapping("/comment-likes/{id}")
    public Result<Void> commentLikeDelete(@PathVariable long id) {
        if (commentLikeAdminMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
        return Result.success();
    }

    // --- post like ---
    @GetMapping("/post-likes")
    public Result<PageResult<PostLikeRelation>> postLikesPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        int pi = p(page);
        int si = s(size);
        long total = postLikeAdminMapper.countAll();
        if (total == 0) {
            return Result.success(new PageResult<>(0, Collections.emptyList()));
        }
        return Result.success(new PageResult<>(total, postLikeAdminMapper.selectPage((pi - 1) * si, si)));
    }

    @GetMapping("/post-likes/{id}")
    public Result<PostLikeRelation> postLikeGet(@PathVariable long id) {
        PostLikeRelation r = postLikeAdminMapper.selectById(id);
        if (r == null) {
            throw new BaseException("记录不存在");
        }
        return Result.success(r);
    }

    @PostMapping("/post-likes")
    public Result<Map<String, Long>> postLikeCreate(@RequestBody PostLikeRelation body) {
        postLikeAdminMapper.insert(body);
        if (body.getId() == null) {
            throw new BaseException("创建失败");
        }
        return Result.success(Map.of("id", body.getId()));
    }

    @PutMapping("/post-likes/{id}")
    public Result<Void> postLikeUpdate(@PathVariable long id, @RequestBody PostLikeRelation body) {
        if (postLikeAdminMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        body.setId(id);
        postLikeAdminMapper.updateRow(body);
        return Result.success();
    }

    @DeleteMapping("/post-likes/{id}")
    public Result<Void> postLikeDelete(@PathVariable long id) {
        if (postLikeAdminMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
        return Result.success();
    }
}
