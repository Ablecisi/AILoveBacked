package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AiCharacterMapper;
import com.ablecisi.ailovebacked.mapper.PromptTemplateMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import com.ablecisi.ailovebacked.pojo.entity.User;
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
    private final UserMapper userMapper;

    /**
     * 渲染角色对话
     *
     * @param role         角色
     * @param emo          用户情绪
     * @param profileBrief 用户简介
     * @param lastDialogue 上一次对话
     * @param userText     用户输入
     * @param userId       当前用户 ID（须由调用方传入；异步线程中不能使用 ThreadLocal）
     * @return 角色对话
     */
    public String renderWithCharacter(AiCharacterVO role,
                                      EmotionClient.EmotionDTO emo,
                                      String profileBrief,
                                      String lastDialogue,
                                      String userText,
                                      Long userId,
                                      String sessionScene) {
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }
        if (emo == null) {
            throw new IllegalArgumentException("情绪识别结果为空");
        }

        String roleType = (role.getTypeName() == null || role.getTypeName().isBlank())
                ? "companion" : role.getTypeName();

        String tpl = Optional.ofNullable(templateMapper.selectActiveByRole(roleType))
                .map(PromptTemplate::getTemplate)
                .orElse(DEFAULT_TEMPLATE);
        User user = userId == null ? null : userMapper.getUserById(userId);
        String userDisplayName = nv(user == null ? null : user.getName());

        return tpl.replace("{style}", resolvePromptStyle(role))
                .replace("{name}", nv(role.getName()))
                .replace("{gender}", role.getGender() == null ? "未知" : role.getGender() == 1 ? "男" : role.getGender() == 2 ? "女" : "其他")
                .replace("{traits}", nv(role.getTraits()))
                .replace("{persona_desc}", nv(role.getPersonaDesc()))
                .replace("{interests}", nv(role.getInterests()))
                .replace("{backstory}", nv(role.getBackstory()))
                .replace("{emotion}", nv(emo.getEmotion()))
                .replace("{confidence}", String.format("%.2f", emo.getConfidence() == null ? 0.0 : emo.getConfidence()))
                .replace("{profile}", nv(profileBrief))
                .replace("{dialogue}", nv(lastDialogue))
                .replace("{user_text}", nv(userText))
                .replace("{user_name}", userDisplayName)
                .replace("{scene}", nv(sessionScene))
                ;
    }

    /**
     * 渲染角色对话
     *
     * @param characterId 角色ID
     * @param emo         用户情绪
     * @param profileBrief 用户简介
     * @param lastDialogue 上一次对话
     * @param userText    用户输入
     * @param userId      当前用户 ID
     * @return 角色对话
     */
    public String renderWithCharacter(Long characterId,
                                      EmotionClient.EmotionDTO emo,
                                      String profileBrief,
                                      String lastDialogue,
                                      String userText,
                                      Long userId,
                                      String sessionScene) {
        AiCharacterVO role = aiCharacterMapper.selectById(characterId);
        return renderWithCharacter(role, emo, profileBrief, lastDialogue, userText, userId, sessionScene);
    }

    /**
     * 删除空格、换行符、制表符
     *
     * @param s 字符串
     * @return 处理后的字符串
     */
    private String nv(String s) {
        return (s == null || s.isBlank()) ? "（无）" : s;
    }

    /**
     * 类型表 {@code prompt_style} 非空时优先；否则全局默认（与未配置类型的角色一致）。
     */
    private String resolvePromptStyle(AiCharacterVO role) {
        if (role != null && role.getTypePromptStyle() != null && !role.getTypePromptStyle().isBlank()) {
            return role.getTypePromptStyle().trim();
        }
        return DEFAULT_PROMPT_STYLE;
    }

    private static final String DEFAULT_PROMPT_STYLE =
            "自然、友好、简洁，先给结论后给理由，提供1到3条可执行建议。";

    /**
     * 默认模板
     */
    private static final String DEFAULT_TEMPLATE = """
            AI设定部分开始：
                你是一位{style}的人
                你的名字是「{name}」
                你的性别是：{gender}。
                你的性格特点：{traits}
                你的角色深度设定（persona）：{persona_desc}
                你的兴趣爱好：{interests}
                你的背景故事：{backstory}
            AI设定部分结束。
            
            当前会话场景与背景（须始终遵守）：{scene}
            
            用户设定部分开始：
                用户的姓名为：「{user_name}」
                用户现在说的话：{user_text}
                用户当前情绪：{emotion}（置信度：{confidence}）
                用户画像摘要：{profile}
                用户最近与AI的对话（节选）：{dialogue}
            用户设定部分结束。
            
            请先共情，再给出轻量建议；语气自然真诚，避免机械与重复人设，控制在合适篇幅。
            注意：用户当前的情绪是用于通过用户情绪来调整你的语气和建议的，不是让你去描述或分析用户的情绪。
            你的回复必须严格基于以上信息，且不能凭空捏造事实。
            与你对话的人类就是上面提到的用户，请务必尊重和保护用户隐私，不要泄露任何个人信息。
            """;
}

