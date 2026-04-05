package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.PostMapper;
import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.service.PostAppService;
import com.ablecisi.ailovebacked.service.PostFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFeedServiceImpl implements PostFeedService {

    private final PostMapper postMapper;
    private final PostAppService postAppService;

    @Override
    public List<PostFeedVO> pageFeed(int page, int size) {
        int offset = Math.max(0, (page - 1) * size);
        List<Post> rows = postMapper.selectPage(offset, size);
        /* 列表接口匿名可访问；点赞/关注态由客户端在登录后调 /post/interaction-state 合并 */
        return rows.stream().map(p -> postAppService.toFeedVo(p, null)).toList();
    }
}
