package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminArticleListVO;
import com.ablecisi.ailovebacked.result.PageResult;

public interface AdminArticleManageService {
    PageResult<AdminArticleListVO> page(int page, int size, String keyword);

    Article get(Long id);

    long create(Article article);

    void update(Long id, Article article);

    void delete(Long id);
}
