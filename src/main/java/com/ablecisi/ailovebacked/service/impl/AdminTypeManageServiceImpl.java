package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.AppTypeMapper;
import com.ablecisi.ailovebacked.pojo.entity.TypeItem;
import com.ablecisi.ailovebacked.service.AdminTypeManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTypeManageServiceImpl implements AdminTypeManageService {

    private final AppTypeMapper appTypeMapper;

    @Override
    public List<TypeItem> listAll() {
        return appTypeMapper.selectAll();
    }

    @Override
    public TypeItem get(Long id) {
        TypeItem t = appTypeMapper.selectById(id);
        if (t == null) {
            throw new BaseException("类型不存在");
        }
        return t;
    }

    @Override
    @Transactional
    public long create(String name) {
        if (name == null || name.isBlank()) {
            throw new BaseException("名称不能为空");
        }
        TypeItem row = TypeItem.builder().name(name.trim()).build();
        appTypeMapper.insert(row);
        if (row.getId() == null) {
            throw new BaseException("创建失败");
        }
        return row.getId();
    }

    @Override
    @Transactional
    public void update(Long id, String name) {
        TypeItem t = appTypeMapper.selectById(id);
        if (t == null) {
            throw new BaseException("类型不存在");
        }
        if (name == null || name.isBlank()) {
            throw new BaseException("名称不能为空");
        }
        t.setName(name.trim());
        appTypeMapper.updateRow(t);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (appTypeMapper.deleteById(id) == 0) {
            throw new BaseException("类型不存在或已被引用");
        }
    }
}
