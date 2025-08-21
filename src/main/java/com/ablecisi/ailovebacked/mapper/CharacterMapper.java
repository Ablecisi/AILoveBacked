package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.AiCharacter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 * 角色数据访问层接口
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:30
 **/
@Mapper
public interface CharacterMapper {
    /**
     * 获取热门角色列表
     *
     * @return 热门角色实体对象列表
     */
    List<AiCharacter> getHotCharacters();

    /**
     * 根据角色ID获取角色详情
     *
     * @param characterId 角色ID
     * @return 角色实体对象
     */
    AiCharacter getCharacterById(Long characterId);
}
