package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.CharacterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AILoveBacked
 * <br>
 * com.ablecisi.ailovebacked.controller <br>
 * 角色控制器
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:11
 **/
@RestController
@RequestMapping("/character")
@Slf4j
public class CharacterController {
    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * 获取热门角色
     *
     * @return 热门角色列表
     */
    @GetMapping("/hot")
    public Result<List<AiCharacterVO>> getHotCharacters() {
        log.info("获取热门角色");
        List<AiCharacterVO> hotCharacters = characterService.getHotCharacters();
        if (hotCharacters.isEmpty()) {
            return Result.error("没有热门角色");
        }
        log.info("成功获取热门角色 {} ", hotCharacters);
        return Result.success(hotCharacters);
    }

    /**
     * 获取角色详情
     *
     * @param characterId 角色ID
     * @return 角色详情
     */
    @GetMapping()
    public Result<AiCharacterVO> getCharacterById(@RequestParam String characterId) {
        log.info("获取角色详情，角色ID: {}", characterId);
        AiCharacterVO character = characterService.getCharacterById(Long.parseLong(characterId));
        if (character == null) {
            return Result.error("角色不存在");
        }
        log.info("成功获取角色详情: {}", character);
        return Result.success(character);
    }
}
