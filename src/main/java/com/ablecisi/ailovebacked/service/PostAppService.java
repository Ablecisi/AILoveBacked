package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.dto.PostCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.PostLikeDTO;
import com.ablecisi.ailovebacked.pojo.entity.Post;
import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;
import com.ablecisi.ailovebacked.pojo.vo.PostInteractionStateVO;
import com.ablecisi.ailovebacked.pojo.vo.PostLikeResultVO;

import java.util.List;

public interface PostAppService {

    /**
     * 列表/详情共用：根据浏览者 ID 填充 liked、authorFollowed
     */
    PostFeedVO toFeedVo(Post post, Long viewerUserId);

    PostFeedVO getDetail(Long postId, Long viewerUserId);

    long createPost(Long userId, PostCreateDTO dto);

    PostLikeResultVO toggleLike(Long userId, PostLikeDTO dto);

    void recordShare(Long postId);

    List<PostInteractionStateVO> interactionStates(Long userId, List<Long> postIds);
}
