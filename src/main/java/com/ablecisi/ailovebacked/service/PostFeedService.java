package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.PostFeedVO;

import java.util.List;

public interface PostFeedService {

    List<PostFeedVO> pageFeed(int page, int size);
}
