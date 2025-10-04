package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.context.BaseContext;
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
        User user = userMapper.getUserById(BaseContext.getCurrentId());

        return tpl.replace("{style}", mapStyle(roleType))
                .replace("{name}", nv(role.getName()))
                .replace("{gender}", role.getGender() == null ? "未知" : role.getGender() == 1 ? "男" : role.getGender() == 2 ? "女" : "其他")
                .replace("{traits}", nv(role.getTraits()))
                .replace("{interests}", nv(role.getInterests()))
                .replace("{backstory}", nv(role.getBackstory()))
                .replace("{emotion}", nv(emo.getEmotion()))
                .replace("{confidence}", String.format("%.2f", emo.getConfidence() == null ? 0.0 : emo.getConfidence()))
                .replace("{profile}", nv(profileBrief))
                .replace("{dialogue}", nv(lastDialogue))
                .replace("{user_text}", nv(userText))
                .replace("{user_name}", nv(user.getName()))
                ;
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
        if (type == null || type.isBlank()) return "自然、友好、简洁，先给结论后给理由，提供1到3条可执行建议。";
        return switch (type.trim()) {
            case "情感陪伴", "companion" ->
                    "温柔共情与支持性语气，优先倾听与安慰，避免评判与命令；多用第一人称与“你”的称呼，短句、慢节奏，必要时给出小而具体的可执行建议。";
            case "学习助手", "study_assistant" ->
                    "清晰、耐心、循序渐进；先给结论再给步骤，配合小例子与类比解释，分点陈述要点，鼓励提问与自我检查。";
            case "职业导师", "career_mentor" ->
                    "专业、务实、结果导向；先澄清目标与约束，再给可执行方案与风险提示；提供模板、清单与里程碑，结合最佳实践与案例。";
            case "知心朋友", "close_friend" ->
                    "轻松、真诚、像朋友聊天；先认可与共情，再给中肯建议；适度口语与幽默，避免说教与打断。";
            case "生活顾问", "life_advisor" ->
                    "实用、具体、可落地；倾向清单化步骤与注意事项，结合日常场景与预算建议，提供替代方案与优先级。";
            case "创意伙伴", "creative_partner" ->
                    "发散思维与启发式共创；鼓励“是的，而且”的延展；先广泛点子后收敛评估，给多风格与跨界组合，并标注可行性与下一步。";
            default -> "自然、友好、简洁，先给结论后给理由，提供1到3条可执行建议。";
        };
    }

    private static final String DEFAULT_TEMPLATE = """
            
            AI设定部分开始：
                你是一位{style}的人
                你的名字是「{name}」
                你的性别是：{gender}。
                你的性格特点：{traits}
                你的兴趣爱好：{interests}
                你的背景故事：{backstory}
            AI设定部分结束。
            
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

