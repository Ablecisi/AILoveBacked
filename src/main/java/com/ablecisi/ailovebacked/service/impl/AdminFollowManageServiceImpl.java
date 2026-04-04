package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.FollowRelationMapper;
import com.ablecisi.ailovebacked.pojo.entity.FollowRelation;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminFollowManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminFollowManageServiceImpl implements AdminFollowManageService {

    private final FollowRelationMapper followRelationMapper;

    @Override
    public PageResult<FollowRelation> page(int page, int size) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        long total = followRelationMapper.countAll();
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        int offset = (p - 1) * s;
        List<FollowRelation> records = followRelationMapper.selectPage(offset, s);
        return new PageResult<>(total, records);
    }

    @Override
    public FollowRelation get(Long id) {
        FollowRelation r = followRelationMapper.selectById(id);
        if (r == null) {
            throw new BaseException("记录不存在");
        }
        return r;
    }

    @Override
    @Transactional
    public long create(FollowRelation row) {
        if (row.getFollowingId() == null || row.getFollowerId() == null) {
            throw new BaseException("followingId 与 followerId 不能为空");
        }
        followRelationMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public void update(Long id, FollowRelation row) {
        if (followRelationMapper.selectById(id) == null) {
            throw new BaseException("记录不存在");
        }
        row.setId(id);
        followRelationMapper.updateRow(row);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (followRelationMapper.deleteById(id) == 0) {
            throw new BaseException("记录不存在");
        }
    }
}
