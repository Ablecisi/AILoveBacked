package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.mapper.PromptTemplateMapper;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * PromptService
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:44
 **/
@Service
@RequiredArgsConstructor
public class PromptService {
    private final PromptTemplateMapper templateMapper;
    private final AiCharacterMapper aiCharacterMapper;

    public String renderWithCharacter(AiCharacterVO role,
                                      EmotionClient.EmotionDTO emo,
                                      String profileBrief,
                                      String lastDialogue,
                                      String userText) {

        String roleType = (role.getTypeName() == null || role.getTypeName().isBlank())
                ? "companion" : role.getTypeName();

        String tpl = Optional.ofNullable(templateMapper.selectActiveByRole(roleType))
                .map(PromptTemplate::getTemplate)
                .orElse(DEFAULT_TEMPLATE);

        return tpl.replace("{style}", mapStyle(roleType))
                .replace("{name}", nv(role.getName()))
                .replace("{gender}", role.getGender() == null ? "未知" : String.valueOf(role.getGender()))
                .replace("{traits}", nv(role.getTraits()))
                .replace("{interests}", nv(role.getInterests()))
                .replace("{backstory}", nv(role.getBackstory()))
                .replace("{emotion}", nv(emo.getEmotion()))
                .replace("{confidence}", String.format("%.2f", emo.getConfidence() == null ? 0.0 : emo.getConfidence()))
                .replace("{profile}", nv(profileBrief))
                .replace("{dialogue}", nv(lastDialogue))
                .replace("{user_text}", nv(userText));
    }

    public String renderWithCharacter(Long characterId,
                                      EmotionClient.EmotionDTO emo,
                                      String profileBrief,
                                      String lastDialogue,
                                      String userText) {
        AiCharacterVO role = aiCharacterMapper.selectById(characterId);
        return renderWithCharacter(role, emo, profileBrief, lastDialogue, userText);
    }

    private String nv(String s) {
        return (s == null || s.isBlank()) ? "（无）" : s;
    }

    private String mapStyle(String type) {
        return switch (type.toLowerCase()) {
            case "mentor" -> "理性专业、善于启发的导师";
            case "lover" -> "亲密、体贴、细腻的恋人";
            case "coach" -> "鼓励、积极、目标导向的教练";
            default -> "温柔体贴、共情力强的朋友";
        };
    }

    private static final String DEFAULT_TEMPLATE = """
            你是一位{style}，名字「{name}」，性别：{gender}。
            性格特点：{traits}
            兴趣爱好：{interests}
            背景故事（仅用于语气风格，不可虚构事实）：{backstory}
            
            用户当前情绪：{emotion}（置信度：{confidence}）
            用户画像摘要：{profile}
            最近对话（节选）：
            {dialogue}
            
            用户说：{user_text}
            
            请先共情，再给出轻量建议；语气自然真诚，避免机械与重复人设，控制在合适篇幅。
            """;
}

