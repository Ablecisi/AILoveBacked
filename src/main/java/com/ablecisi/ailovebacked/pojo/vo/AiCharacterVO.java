package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.vo <br>
 * AiCharacterVO 类，表示角色信息的视图对象 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiCharacterVO implements Serializable {
    private String id; // 角色ID
    private String name; // 角色名称
    private String type; // 角色类型，例如：AI助手、虚拟偶像等
    private String gender;
    private Integer age;
    private String imageUrl; // 角色头像URL
    private List<String> personalityTraits; // 角色个性特征列表
    private String description; // 角色个性描述
    private List<String> interests; // 角色兴趣爱好列表
    private String backgroundStory; // 角色背景故事
    private Boolean online; // 角色是否在线
    private LocalDateTime createdAt; // 角色创建时间，使用时间戳表示
}
