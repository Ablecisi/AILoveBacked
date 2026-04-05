package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.entity.TypeItem;

import java.util.List;

public interface AdminTypeManageService {
    List<TypeItem> listAll();

    TypeItem get(Long id);

    long create(String name, String promptStyle);

    void update(Long id, String name, String promptStyle);

    void delete(Long id);
}
