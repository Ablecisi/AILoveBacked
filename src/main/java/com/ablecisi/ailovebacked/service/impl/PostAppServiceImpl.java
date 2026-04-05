package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.PostLikeRelationMapper;
import com.ablecisi.ailovebacked.mapper.PostMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.dto.PostCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.PostLikeDTO;
import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.pojo.entity.PostLikeRelation;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.pojo.vo.PostInteractionStateVO;
import com.ablecisi.ailovebacked.pojo.vo.PostLikeResultVO;
import com.ablecisi.ailovebacked.service.PostAppService;
import com.ablecisi.ailovebacked.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostAppServiceImpl implements PostAppService {

    private final PostMapper postMapper;
    private final PostLikeRelationMapper postLikeRelationMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public PostFeedVO getDetail(Long postId, Long viewerUserId) {
        Post p = postMapper.selectById(postId);
        if (p == null) {
            throw new BaseException("帖子不存在");
        }
        postMapper.incrementViewCount(postId);
        p = postMapper.selectById(postId);
        return toFeedVo(p, viewerUserId);
    }

    @Override
    public PostFeedVO toFeedVo(Post p, Long viewerUserId) {
        return buildFeedVo(p, viewerUserId);
    }

    @Override
    @Transactional
    public long createPost(Long userId, PostCreateDTO dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BaseException("内容不能为空");
        }
        Post row = Post.builder()
                .userId(userId)
                .content(dto.getContent().trim())
                .imageUrls(dto.getImageUrls() != null ? dto.getImageUrls() : List.of())
                .tags(dto.getTags() != null ? dto.getTags() : List.of())
                .viewCount(0)
                .likeCount(0)
                .shareCount(0)
                .commentCount(0)
                .build();
        postMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("发布失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public PostLikeResultVO toggleLike(Long userId, PostLikeDTO dto) {
        Post p = postMapper.selectById(dto.getPostId());
        if (p == null) {
            throw new BaseException("帖子不存在");
        }
        boolean want = Boolean.TRUE.equals(dto.getLike());
        int exists = postLikeRelationMapper.countByUserAndPost(userId, dto.getPostId());
        if (want && exists == 0) {
            postLikeRelationMapper.insert(PostLikeRelation.builder()
                    .userId(userId)
                    .postId(dto.getPostId())
                    .build());
            postMapper.incrementLikeCount(dto.getPostId());
        } else if (!want && exists > 0) {
            postLikeRelationMapper.deleteByUserAndPost(userId, dto.getPostId());
            postMapper.decrementLikeCount(dto.getPostId());
        }
        Post after = postMapper.selectById(dto.getPostId());
        int lc = after != null && after.getLikeCount() != null ? after.getLikeCount() : 0;
        boolean liked = postLikeRelationMapper.countByUserAndPost(userId, dto.getPostId()) > 0;
        return new PostLikeResultVO(lc, liked);
    }

    @Override
    public void recordShare(Long postId) {
        if (postMapper.selectById(postId) == null) {
            throw new BaseException("帖子不存在");
        }
        postMapper.incrementShareCount(postId);
    }

    @Override
    public List<PostInteractionStateVO> interactionStates(Long userId, List<Long> postIds) {
        List<PostInteractionStateVO> out = new ArrayList<>();
        if (userId == null || postIds == null) {
            return out;
        }
        for (Long id : postIds) {
            if (id == null) {
                continue;
            }
            Post p = postMapper.selectById(id);
            if (p == null) {
                continue;
            }
            boolean liked = postLikeRelationMapper.countByUserAndPost(userId, id) > 0;
            boolean following = p.getUserId() != null
                    && Boolean.TRUE.equals(userService.isFollowing(userId, p.getUserId()));
            out.add(PostInteractionStateVO.builder()
                    .postId(String.valueOf(id))
                    .liked(liked)
                    .followingAuthor(following)
                    .build());
        }
        return out;
    }

    private PostFeedVO buildFeedVo(Post p, Long viewerUserId) {
        User u = p.getUserId() != null ? userMapper.getUserById(p.getUserId()) : null;
        String name = u != null && u.getName() != null && !u.getName().isBlank()
                ? u.getName()
                : (u != null && u.getUsername() != null ? u.getUsername() : "");
        String avatar = u != null && u.getAvatarUrl() != null ? u.getAvatarUrl() : "";
        Boolean liked = false;
        Boolean authorFollowed = null;
        if (viewerUserId != null) {
            liked = postLikeRelationMapper.countByUserAndPost(viewerUserId, p.getId()) > 0;
            if (p.getUserId() != null) {
                authorFollowed = Boolean.TRUE.equals(userService.isFollowing(viewerUserId, p.getUserId()));
            }
        }
        return PostFeedVO.builder()
                .id(String.valueOf(p.getId()))
                .authorId(p.getUserId() != null ? String.valueOf(p.getUserId()) : "")
                .authorName(name)
                .authorAvatarUrl(avatar)
                .content(p.getContent())
                .imageUrls(p.getImageUrls() != null ? p.getImageUrls() : List.of())
                .tags(p.getTags() != null ? p.getTags() : List.of())
                .likeCount(p.getLikeCount())
                .commentCount(p.getCommentCount())
                .shareCount(p.getShareCount())
                .createdAt(p.getCreateTime())
                .liked(liked)
                .authorFollowed(authorFollowed)
                .build();
    }
}
