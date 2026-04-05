package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.PostMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.service.PostFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFeedServiceImpl implements PostFeedService {

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    public List<PostFeedVO> pageFeed(int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        List<Post> rows = postMapper.selectPage(offset, size);
        return rows.stream().map(this::toVo).toList();
    }

    private PostFeedVO toVo(Post p) {
        User u = p.getUserId() != null ? userMapper.getUserById(p.getUserId()) : null;
        String name = u != null && u.getName() != null && !u.getName().isBlank()
                ? u.getName()
                : (u != null ? u.getUsername() : "");
        String avatar = u != null && u.getAvatarUrl() != null ? u.getAvatarUrl() : "";
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
                .liked(false)
                .build();
    }
}
