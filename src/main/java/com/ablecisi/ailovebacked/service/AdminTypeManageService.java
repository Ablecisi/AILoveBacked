package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.TypeItem;

import java.util.List;

public interface AdminTypeManageService {
    List<TypeItem> listAll();

    TypeItem get(Long id);

    long create(String name);

    void update(Long id, String name);

    void delete(Long id);
}
