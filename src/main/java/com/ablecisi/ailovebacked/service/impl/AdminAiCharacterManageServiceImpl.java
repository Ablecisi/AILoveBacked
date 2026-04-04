package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.dto.admin.AdminAiCharacterWriteDTO;
import com.ablecisi.ailovebacked.pojo.entity.AiCharacter;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.PageResult;
import com.ablecisi.ailovebacked.service.AdminAiCharacterManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAiCharacterManageServiceImpl implements AdminAiCharacterManageService {

    private final AiCharacterMapper aiCharacterMapper;

    @Override
    public PageResult<AiCharacterVO> page(int page, int size, String keyword, Long typeId, Integer status) {
        int p = Math.max(page, 1);
        int s = Math.min(Math.max(size, 1), 100);
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        AiCharacterQueryDTO q = new AiCharacterQueryDTO();
        q.setUserId(null);
        q.setKeyword(kw);
        q.setTypeId(typeId);
        q.setStatus(status);
        q.setPage(p);
        q.setSize(s);
        q.setOffset((p - 1) * s);
        q.setOrderBy("update_time");
        q.setOrder("DESC");
        long total = aiCharacterMapper.pageCount(q);
        if (total == 0) {
            return new PageResult<>(0, Collections.emptyList());
        }
        List<AiCharacterVO> records = aiCharacterMapper.pageQuery(q);
        return new PageResult<>(total, records);
    }

    @Override
    public AiCharacterVO get(Long id) {
        AiCharacterVO v = aiCharacterMapper.selectById(id);
        if (v == null) {
            throw new BaseException("角色不存在");
        }
        return v;
    }

    @Override
    @Transactional
    public long create(AdminAiCharacterWriteDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BaseException("角色名称不能为空");
        }
        AiCharacter po = AiCharacter.builder()
                .userId(dto.getUserId())
                .name(dto.getName().trim())
                .typeId(dto.getTypeId())
                .gender(dto.getGender() != null ? dto.getGender() : 2)
                .age(dto.getAge())
                .imageUrl(dto.getImageUrl())
                .traits(dto.getTraits())
                .personaDesc(dto.getPersonaDesc())
                .interests(dto.getInterests())
                .backstory(dto.getBackstory())
                .online(dto.getOnline() != null ? dto.getOnline() : 0)
                .status(dto.getStatus() != null ? dto.getStatus() : 1)
                .build();
        aiCharacterMapper.insert(po);
        if (po.getId() == null) {
            throw new BaseException("创建失败");
        }
        return po.getId();
    }

    @Override
    @Transactional
    public void update(Long id, AdminAiCharacterWriteDTO dto) {
        AiCharacter cur = aiCharacterMapper.selectEntityById(id);
        if (cur == null) {
            throw new BaseException("角色不存在");
        }
        if (dto.getUserId() != null) {
            cur.setUserId(dto.getUserId());
        }
        if (dto.getName() != null) {
            cur.setName(dto.getName().trim());
        }
        if (dto.getTypeId() != null) {
            cur.setTypeId(dto.getTypeId());
        }
        if (dto.getGender() != null) {
            cur.setGender(dto.getGender());
        }
        if (dto.getAge() != null) {
            cur.setAge(dto.getAge());
        }
        if (dto.getImageUrl() != null) {
            cur.setImageUrl(dto.getImageUrl());
        }
        if (dto.getTraits() != null) {
            cur.setTraits(dto.getTraits());
        }
        if (dto.getPersonaDesc() != null) {
            cur.setPersonaDesc(dto.getPersonaDesc());
        }
        if (dto.getInterests() != null) {
            cur.setInterests(dto.getInterests());
        }
        if (dto.getBackstory() != null) {
            cur.setBackstory(dto.getBackstory());
        }
        if (dto.getOnline() != null) {
            cur.setOnline(dto.getOnline());
        }
        if (dto.getStatus() != null) {
            cur.setStatus(dto.getStatus());
        }
        aiCharacterMapper.update(cur);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (aiCharacterMapper.deleteById(id) == 0) {
            throw new BaseException("角色不存在");
        }
    }
}
