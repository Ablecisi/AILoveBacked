package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.constant.OnlineTypeConstant;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.CharacterMapper;
import com.ablecisi.ailovebacked.mapper.TypeMapper;
import com.ablecisi.ailovebacked.pojo.entity.AiCharacter;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:31
 **/
@Service
public class CharacterServiceImpl implements CharacterService {
    private final CharacterMapper characterMapper;
    private final TypeMapper typeMapper;

    @Autowired
    public CharacterServiceImpl(CharacterMapper characterMapper, TypeMapper typeMapper) {
        this.characterMapper = characterMapper;
        this.typeMapper = typeMapper;
    }

    /**
     * 获取热门角色列表
     *
     * @return 热门角色视图对象列表
     */
    @Override
    @Transactional
    public List<AiCharacterVO> getHotCharacters() {
        List<AiCharacter> aiCharacters = characterMapper.getHotCharacters();

        return aiCharacters.stream()
                .map(aiCharacter -> new AiCharacterVO(
                        String.valueOf(aiCharacter.getId()),
                        aiCharacter.getName(),
                        typeMapper.getTypeNameById(aiCharacter.getTypeId()),
                        aiCharacter.getGender() == 1 ? "男" : aiCharacter.getGender() == 2 ? "女" : "其他",
                        aiCharacter.getAge(),
                        aiCharacter.getImageUrl(),
                        aiCharacter.getPersonalityTraits(),
                        aiCharacter.getDescription(),
                        aiCharacter.getInterests(),
                        aiCharacter.getBackgroundStory(),
                        aiCharacter.getOnline() == OnlineTypeConstant.ONLINE,
                        aiCharacter.getCreateTime()

                )).toList();
    }

    /**
     * 根据角色ID获取角色详情
     *
     * @param characterId 角色ID
     * @return 角色视图对象
     */
    @Override
    @Transactional
    public AiCharacterVO getCharacterById(Long characterId) {
        AiCharacter aiCharacter = characterMapper.getCharacterById(characterId);
        if (aiCharacter == null) {
            throw new BaseException("角色不存在");
        }

        return AiCharacterVO.builder()
                .id(String.valueOf(aiCharacter.getId()))
                .name(aiCharacter.getName())
                .type(typeMapper.getTypeNameById(aiCharacter.getTypeId()))
                .gender(aiCharacter.getGender() == 1 ? "男" : aiCharacter.getGender() == 2 ? "女" : "其他")
                .age(aiCharacter.getAge())
                .imageUrl(aiCharacter.getImageUrl())
                .personalityTraits(aiCharacter.getPersonalityTraits())
                .description(aiCharacter.getDescription())
                .interests(aiCharacter.getInterests())
                .backgroundStory(aiCharacter.getBackgroundStory())
                .online(aiCharacter.getOnline() == OnlineTypeConstant.ONLINE)
                .createdAt(aiCharacter.getCreateTime())
                .build();

    }
}
