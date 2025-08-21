package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.mapper.CommentLikeRelationMapper;
import com.ablecisi.ailovebacked.mapper.CommentMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.entity.Comment;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.CommentVO;
import com.ablecisi.ailovebacked.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 * 评论服务实现类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 14:28
 **/
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final CommentLikeRelationMapper commentLikeRelationMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, UserMapper userMapper, CommentLikeRelationMapper commentLikeRelationMapper) {
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.commentLikeRelationMapper = commentLikeRelationMapper;
    }

    /**
     * 根据文章ID获取评论列表
     *
     * @param articleId 文章ID
     * @return 评论列表
     */
    @Override
    public List<CommentVO> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentMapper.getCommentsByArticleId(articleId);

        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();
        // 如果没有评论，返回空列表
        List<CommentVO> commentVOs;
        Long currentUserId = BaseContext.getCurrentId();
        if (!comments.isEmpty()) {
            commentVOs = comments.stream()
                    .map(comment -> {
                        User commentUser = userMapper.getUserById(Long.parseLong(comment.getUserId()));
                        return CommentVO.builder()
                                .id(String.valueOf(comment.getId()))
                                .content(comment.getContent())
                                .userId(String.valueOf(comment.getUserId()))
                                .userName(commentUser.getName())
                                .userAvatarUrl(commentUser.getAvatarUrl())
                                .parentId(String.valueOf(comment.getParentId()))
                                .replies(null) // 暂时不处理回复
                                .createTime(comment.getCreateTime().toEpochSecond(ZoneOffset.UTC) * 1000) // 转换为时间戳
                                .build();
                    })
                    .toList();

            // 检查每个评论是否被用户点赞
            for (CommentVO commentVO : commentVOs) {
                // 获取用户点赞的评论ID列表
                List<Long> likedComments = commentLikeRelationMapper.getLikedCommentIdsByUserId(currentUserId);
                // 检查评论是否被用户点赞
                boolean isLiked = likedComments.contains(Long.parseLong(commentVO.getId()));
                commentVO.setIsLiked(isLiked);
            }
            // 设置评论的点赞数
            for (CommentVO commentVO : commentVOs) {
                Long commentId = Long.parseLong(commentVO.getId());
                Integer likeCount = commentLikeRelationMapper.getLikeCountByCommentId(commentId);
                commentVO.setLikeCount(likeCount != null ? likeCount : 0);
            }
        } else {
            // 如果没有评论，返回空列表
            commentVOs = List.of();
        }

        return commentVOs;
    }
}
