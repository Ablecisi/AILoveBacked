package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.ArticleMapper;
import com.ablecisi.ailovebacked.pojo.entity.Article;
import com.ablecisi.ailovebacked.pojo.vo.admin.AdminArticleListVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminArticleManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminArticleManageServiceImpl implements AdminArticleManageService {

    private final ArticleMapper articleMapper;

    @Override
    public PageResult<AdminArticleListVO> page(int page, int size, String keyword) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = articleMapper.countPageForAdmin(kw);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<AdminArticleListVO> records = articleMapper.pageForAdmin(kw, offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public Article get(Long id) {
        Article a = articleMapper.selectById(id);
        if (a == null) {
            throw new BaseException("文章不存在");
        }
        return a;
    }

    @Override
    @Transactional
    public long create(Article article) {
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new BaseException("标题不能为空");
        }
        if (article.getContent() == null) {
            throw new BaseException("正文不能为空");
        }
        if (article.getUserId() == null) {
            throw new BaseException("作者 userId 不能为空");
        }
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        if (article.getLikeCount() == null) {
            article.setLikeCount(0);
        }
        if (article.getCommentCount() == null) {
            article.setCommentCount(0);
        }
        articleMapper.insert(article);
        if (article.getId() == null) {
            throw new BaseException("创建失败");
        }
        return article.getId();
    }

    @Override
    @Transactional
    public void update(Long id, Article article) {
        if (articleMapper.selectById(id) == null) {
            throw new BaseException("文章不存在");
        }
        article.setId(id);
        articleMapper.updateRow(article);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (articleMapper.deleteById(id) == 0) {
            throw new BaseException("文章不存在");
        }
    }
}
