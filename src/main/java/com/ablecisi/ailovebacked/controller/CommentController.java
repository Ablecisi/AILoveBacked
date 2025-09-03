package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.dto.CreateCommentDTO;
import com.ablecisi.ailovebacked.pojo.dto.LikeDTO;
import com.ablecisi.ailovebacked.pojo.dto.UpdateCommentDTO;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.pojo.vo.RootTreeVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.controller <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/28
 * 星期四
 * 23:57
 **/
@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 创建评论
     *
     * @param dto 创建评论参数
     * @return Result<CreateResp> 创建结果
     */
    @PostMapping("/create")
    public Result<CommentVO> create(@RequestBody @Valid CreateCommentDTO dto) {
        log.info("创建评论: {}", dto);
        CommentVO commentVO = commentService.create(dto);
        // 创建成功后，返回精简信息（前端如需完整可再拉顶层或树）
        return Result.success(commentVO);
    }

    // 方案A：一页两次SQL（顶层+批量子树）

    /**
     * 批量打包评论树
     *
     * @param targetType 目标类型 article/post
     * @param targetId   目标ID
     * @param sort       排序 time/popularity
     * @param page       页码
     * @param size       每页大小
     * @param maxDepth   最大深度（可选）
     * @return Result<PageResult<RootTreeVO>> 评论树列表
     */
    @GetMapping("/bundle")
    public Result<PageResult<RootTreeVO>> bundle(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "time") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer maxDepth) {
        log.info("批量打包评论树: targetType={}, targetId={}, sort={}, page={}, size={}, maxDepth={}",
                targetType, targetId, sort, page, size, maxDepth);
        return Result.success(commentService.bundleByTarget(targetType, targetId, sort, page, size, maxDepth)
        );
    }

    /**
     * 获取顶层评论
     *
     * @param targetType 目标类型 article/post
     * @param targetId   目标ID
     * @param sort       排序 time/popularity
     * @param page       页码
     * @param size       每页大小
     * @return Result<PageResult<CommentVO>> 置顶评论列表
     */
    @GetMapping("/top")
    public Result<PageResult<CommentVO>> top(@RequestParam String targetType,
                                             @RequestParam Long targetId,
                                             @RequestParam(defaultValue = "time") String sort,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("获取顶层评论: targetType={}, targetId={}, sort={}, page={}, size={}",
                targetType, targetId, sort, page, size);
        List<CommentVO> list;
        long total;
        if ("article".equals(targetType)) {
            list = commentService.pageTopByArticle(targetId, sort, page, size);
            total = commentService.countTopByArticle(targetId);
        } else if ("post".equals(targetType)) {
            list = commentService.pageTopByPost(targetId, sort, page, size);
            total = commentService.countTopByPost(targetId);
        } else {
            return Result.error("targetType 必须为 article 或 post");
        }
        return Result.success(new PageResult<>(total, list));
    }

    /**
     * 获取评论树
     *
     * @param rootId    根评论ID
     * @param afterPath 分页路径
     * @param size      数量
     * @return Result<List<CommentVO>> 评论树列表
     */
    @GetMapping("/tree")
    public Result<List<CommentVO>> tree(@RequestParam Long rootId,
                                        @RequestParam(required = false) String afterPath,
                                        @RequestParam(defaultValue = "100") int size) {
        log.info("获取评论树: rootId={}, afterPath={}, size={}", rootId, afterPath, size);
        return Result.success(commentService.listTreeByRoot(rootId, afterPath, size));
    }

    /**
     * 获取子评论（平铺）
     *
     * @param parentId 父评论ID
     * @param page     页码
     * @param size     每页大小
     * @return Result<PageResult<CommentVO>> 子评论列表
     */
    @GetMapping("/children")
    public Result<PageResult<CommentVO>> children(@RequestParam Long parentId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "20") int size) {
        log.info("获取子评论: parentId={}, page={}, size={}", parentId, page, size);
        List<CommentVO> list = commentService.pageChildren(parentId, page, size);
        long total = commentService.countChildren(parentId);
        return Result.success(new PageResult<>(total, list));
    }

    /**
     * 更新评论内容
     *
     * @param dto 更新评论参数
     * @return Result<Boolean> 更新结果
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateCommentDTO dto) {
        log.info("更新评论内容: {}", dto);
        int n = commentService.updateContent(dto);
        return n > 0 ? Result.success("更新成功", true) : Result.error("更新失败", false);
    }

    /**
     * 软删除评论
     *
     * @param id 评论ID
     * @return Result<Boolean> 删除结果
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestParam Long id) {
        log.info("软删除评论: id={}", id);
        int n = commentService.softDelete(id);
        return n > 0 ? Result.success("删除成功", true) : Result.error("删除失败", false);
    }

    /**
     * 点赞评论
     *
     * @param dto 点赞参数
     * @return Result<Boolean> 点赞结果
     */
    @PostMapping("/like")
    public Result<Boolean> like(@RequestBody @Valid LikeDTO dto) {
        log.info("点赞评论: {}", dto);
        boolean ok = commentService.like(dto.getUserId(), dto.getCommentId());
        return ok ? Result.success("点赞成功", true) : Result.success("已点赞过", false);
    }

    /**
     * 取消点赞评论
     *
     * @param dto 取消点赞参数
     * @return Result<Boolean> 取消点赞结果
     */
    @PostMapping("/unlike")
    public Result<Boolean> unlike(@RequestBody @Valid LikeDTO dto) {
        log.info("取消点赞评论: {}", dto);
        boolean ok = commentService.unlike(dto.getUserId(), dto.getCommentId());
        return ok ? Result.success("取消点赞成功", true) : Result.success("原本未点赞", false);
    }
}

