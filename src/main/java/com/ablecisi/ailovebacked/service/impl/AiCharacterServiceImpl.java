package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterCreateDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterUpdateDTO;
import com.ablecisi.ailovebacked.pojo.entity.AiCharacter;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AiCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/7
 * 星期日
 * 14:18
 **/
@Service
@RequiredArgsConstructor
public class AiCharacterServiceImpl implements AiCharacterService {

    private final AiCharacterMapper mapper;

    private static final Set<String> ORDER_BY_WHITELIST = Set.of(
            "update_time", "create_time", "name", "id"
    );

    @Override
    @Transactional
    public Long create(AiCharacterCreateDTO dto) {
        AiCharacter po = new AiCharacter();
        po.setUserId(dto.getUserId());
        po.setName(dto.getName());
        po.setTypeId(dto.getTypeId());
        po.setGender(Optional.ofNullable(dto.getGender()).orElse(2));
        po.setAge(dto.getAge());
        po.setImageUrl(dto.getImageUrl());
        po.setTraits(normalizeJsonString(dto.getTraits()));
        po.setPersonaDesc(dto.getPersonaDesc());
        po.setInterests(normalizeJsonString(dto.getInterests()));
        po.setBackstory(dto.getBackstory());
        po.setOnline(0);
        po.setStatus(Optional.ofNullable(dto.getStatus()).orElse(1));
        mapper.insert(po);
        return po.getId();
    }

    @Override
    @Transactional
    public boolean update(AiCharacterUpdateDTO dto) {
        AiCharacter po = new AiCharacter();
        po.setId(dto.getId());
        po.setName(dto.getName());
        po.setTypeId(dto.getTypeId());
        po.setGender(dto.getGender());
        po.setAge(dto.getAge());
        po.setImageUrl(dto.getImageUrl());
        po.setTraits(normalizeJsonString(dto.getTraits()));
        po.setPersonaDesc(dto.getPersonaDesc());
        po.setInterests(normalizeJsonString(dto.getInterests()));
        po.setBackstory(dto.getBackstory());
        po.setOnline(dto.getOnline());
        po.setStatus(dto.getStatus());
        return mapper.update(po) > 0;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    @Override
    public AiCharacterVO detail(Long id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional
    public PageResult<AiCharacterVO> page(AiCharacterQueryDTO q) {
        // 排序字段白名单，避免 ${} 注入
        if (q.getOrderBy() == null || !ORDER_BY_WHITELIST.contains(q.getOrderBy())) {
            q.setOrderBy("update_time");
        }
        if (!"ASC".equalsIgnoreCase(q.getOrder())) q.setOrder("DESC"); // 默认 DESC
        q.setOffset((q.getPage() - 1) * q.getSize()); // 计算偏移
        long total = mapper.pageCount(q); // 先查总数
        List<AiCharacterVO> list = java.util.Collections.emptyList();
        if (total != 0) {
            list = mapper.pageQuery(q); // 再查数据
        }
        return new PageResult<>(total, list); // 返回
    }

    @Override
    @Transactional
    public boolean setOnline(Long id, Integer online) {
        return mapper.updateOnline(id, (online != null && online == 1) ? 1 : 0) > 0;
    }

    @Override
    @Transactional
    public boolean setStatus(Long id, Integer status) {
        return mapper.updateStatus(id, (status != null && status == 1) ? 1 : 0) > 0;
    }

    @Override
    public List<String> getCharacterTypes() {
        return mapper.getCharacterTypes();
    }

    /**
     * 允许前端传逗号分隔，统一转为 JSON 字符串存库（可选）
     */
    private String normalizeJsonString(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String s = raw.trim();
        if (s.startsWith("[") || s.startsWith("{")) return s; // 已是 JSON
        // 逗号分隔转 JSON 数组
        String[] arr = Arrays.stream(s.split(","))
                .map(String::trim).filter(x -> !x.isEmpty()).toArray(String[]::new);
        return new com.google.gson.Gson().toJson(arr);
    }
}
