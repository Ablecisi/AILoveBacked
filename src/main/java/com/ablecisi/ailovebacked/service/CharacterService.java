package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:31
 **/
public interface CharacterService {
    /**
     * 获取热门角色列表
     *
     * @return 热门角色视图对象列表
     */
    List<AiCharacterVO> getHotCharacters();

    /**
     * 根据角色ID获取角色详情
     *
     * @param characterId 角色ID
     * @return 角色视图对象
     */
    AiCharacterVO getCharacterById(Long characterId);
}
