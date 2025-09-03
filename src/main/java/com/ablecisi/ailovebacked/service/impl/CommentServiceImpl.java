package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.mapper.CommentMapper;
import com.ablecisi.ailovebacked.pojo.dto.CreateCommentDTO;
import com.ablecisi.ailovebacked.pojo.dto.UpdateCommentDTO;
import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.pojo.vo.RootTreeVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/28
 * 星期四
 * 23:53
 **/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    /**
     * 创建评论
     *
     * @param dto 创建评论参数
     * @return CommentVO 创建结果（含用户信息/软删文案）
     */
    @Transactional
    @Override
    public CommentVO create(CreateCommentDTO dto) {
        if (!"article".equals(dto.getTargetType()) && !"post".equals(dto.getTargetType())) {
            throw new IllegalArgumentException("targetType 必须为 article 或 post");
        }
        if (!StringUtils.hasText(dto.getContent().trim())) {
            throw new IllegalArgumentException("评论内容不能为空");
        }

        Comment c = new Comment();
        c.setContent(dto.getContent().trim());
        String userId = String.valueOf(BaseContext.getCurrentId());
        if (userId == null) throw new IllegalArgumentException("未登录或 token 无效");
        c.setUserId(userId);
        if ("article".equals(dto.getTargetType())) c.setArticleId(dto.getTargetId());
        else c.setPostId(dto.getTargetId());
        c.setParentId(dto.getParentId());
        commentMapper.insertBase(c);

        Long newId = c.getId();
        if (dto.getParentId() == null) {
            commentMapper.fillTreeForRoot(newId);
        } else {
            Comment parent = commentMapper.selectTreeByIdForUpdate(dto.getParentId());
            if (parent == null) throw new IllegalArgumentException("父评论不存在");
            // 最多三级回复
            if (parent.getDepth() != null && parent.getDepth() >= 3) {
                throw new IllegalArgumentException("已达最大回复层级");
            }
            String seg = leftPad6(newId); // 当前节点的路径段
            String newPath = (parent.getPath() == null || parent.getPath().isEmpty())
                    ? seg : parent.getPath() + "/" + seg;
            int newDepth = (parent.getDepth() == null ? 0 : parent.getDepth()) + 1;
            Long root = parent.getRootId() == null ? parent.getId() : parent.getRootId();
            commentMapper.fillTreeForChild(newId, root, newPath, newDepth); // 回填树字段
            commentMapper.incReplyCount(parent.getId()); // 父评论回复数 +1
        }
        // ★ 直接返回 CommentVO（含用户信息/软删文案）
        return commentMapper.selectVOById(newId);
    }

    /**
     * 获取顶层评论(分页)
     *
     * @param articleId 文章ID
     * @param sort      排序 time/hot
     * @param page      页码
     * @param size      每页大小
     * @return List<CommentVO> 置顶评论列表
     */
    @Override
    public List<CommentVO> pageTopByArticle(Long articleId, String sort, int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return commentMapper.pageTopByArticle(articleId, sort, offset, size);
    }

    /**
     * 统计文章顶层评论数
     *
     * @param articleId 文章ID
     * @return long 评论数
     */
    @Override
    public long countTopByArticle(Long articleId) {
        return commentMapper.countTopByArticle(articleId);
    }

    /**
     * 获取帖子顶层评论(分页)
     *
     * @param postId 帖子ID
     * @param sort   排序 time/hot
     * @param page   页码
     * @param size   每页大小
     * @return List<CommentVO> 置顶评论列表
     */
    @Override
    public List<CommentVO> pageTopByPost(Long postId, String sort, int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return commentMapper.pageTopByPost(postId, sort, offset, size);
    }

    /**
     * 统计帖子顶层评论数
     *
     * @param postId 帖子ID
     * @return long 评论数
     */
    @Override
    public long countTopByPost(Long postId) {
        return commentMapper.countTopByPost(postId);
    }

    /**
     * 获取评论树（按路径排序）
     *
     * @param rootId    根评论ID
     * @param afterPath 分页路径
     * @param size      数量
     * @return List<CommentVO> 评论树列表
     */
    @Override
    public List<CommentVO> listTreeByRoot(Long rootId, String afterPath, int size) {
        return commentMapper.listTreeByRoot(rootId, afterPath, size);
    }

    /**
     * 方案A：获取评论树（顶层分页 + 批量子树打包）
     *
     * @param targetType 目标类型 article/post
     * @param targetId   目标ID
     * @param sort       排序 time/popularity
     * @param page       页码
     * @param size       每页大小
     * @param maxDepth   子树最大深度（null不限）
     * @return PageResult<RootTreeVO> 评论树列表（含总数）
     */
    @Override
    public PageResult<RootTreeVO> bundleByTarget(String targetType, Long targetId, String sort, int page, int size, Integer maxDepth) {
        // 1) 顶层分页（已有SQL）
        final int offset = Math.max(page - 1, 0) * size;
        final List<CommentVO> roots;
        final long total;
        if ("article".equals(targetType)) {
            roots = commentMapper.pageTopByArticle(targetId, sort, offset, size);
            total = commentMapper.countTopByArticle(targetId);
        } else if ("post".equals(targetType)) {
            roots = commentMapper.pageTopByPost(targetId, sort, offset, size);
            total = commentMapper.countTopByPost(targetId);
        } else {
            throw new IllegalArgumentException("targetType 必须为 article 或 post");
        }
        if (roots.isEmpty()) return new PageResult<>(0, List.of());

        // 2) 一次SQL批量查这一页所有root的子孙
        List<Long> rootIds = roots.stream().map(CommentVO::getRootId).toList();
        List<CommentVO> allDesc = commentMapper.listDescendantsByRoots(rootIds, maxDepth);

        // 3) 分组组装（按 rootId）
        Map<Long, List<CommentVO>> grouped = new LinkedHashMap<>();
        for (CommentVO d : allDesc) {
            grouped.computeIfAbsent(d.getRootId(), k -> new ArrayList<>()).add(d);
        }

        List<RootTreeVO> records = new ArrayList<>(roots.size());
        for (CommentVO root : roots) {
            RootTreeVO item = new RootTreeVO();
            item.setRoot(root);
            item.setDescendants(grouped.getOrDefault(root.getRootId(), List.of()));
            records.add(item);
        }
        return new PageResult<>(total, records);
    }

    /**
     * 获取子评论（平铺，分页）
     *
     * @param parentId 父评论ID
     * @param page     页码
     * @param size     每页大小
     * @return List<CommentVO> 子评论列表
     */
    @Override
    public List<CommentVO> pageChildren(Long parentId, int page, int size) {
        int offset = Math.max(page - 1, 0) * size;
        return commentMapper.pageChildren(parentId, offset, size);
    }

    /**
     * 统计子评论数
     *
     * @param parentId 父评论ID
     * @return long 评论数
     */
    @Override
    public long countChildren(Long parentId) {
        return commentMapper.countChildren(parentId);
    }

    /**
     * 更新评论内容
     *
     * @param dto 更新评论参数
     * @return int 受影响行数
     */
    @Override
    public int updateContent(UpdateCommentDTO dto) {
        return commentMapper.updateContent(dto.getId(), dto.getContent());
    }

    /**
     * 软删除评论
     *
     * @param id 评论ID
     * @return int 受影响行数
     */
    @Override
    public int softDelete(Long id) {
        return commentMapper.softDelete(id);
    }

    /**
     * 点赞评论
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return boolean 点赞结果
     */
    @Transactional
    @Override
    public boolean like(Long userId, Long commentId) {
        int affected = commentMapper.insertLike(userId, commentId);
        if (affected == 1) {
            commentMapper.incLikeCount(commentId);
            return true;
        }
        return false;
    }

    /**
     * 取消点赞评论
     *
     * @param userId    用户ID
     * @param commentId 评论ID
     * @return boolean 取消点赞结果
     */
    @Transactional
    @Override
    public boolean unlike(Long userId, Long commentId) {
        int affected = commentMapper.deleteLike(userId, commentId);
        if (affected > 0) {
            commentMapper.decLikeCount(commentId);
            return true;
        }
        return false;
    }

    /**
     * 左侧补零到6位，用于路径拼接
     *
     * @param id 评论ID
     * @return String 补零后的字符串
     */
    private String leftPad6(Long id) {
        String s = String.valueOf(id);
        return "000000".substring(Math.min(6, s.length())) + s;
    }
}
