package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.entity <br>
 * AiCharacter 实体类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 18:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiCharacter implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id; // 角色ID
    private String name; // 角色名称
    private Long typeId; // 角色类型，例如：AI助手、虚拟偶像等
    private Short gender; // 角色性别，0表示男性，1表示女性，2表示其他
    private Integer age; // 角色年龄，单位为岁
    private String imageUrl; // 角色头像URL
    private List<String> personalityTraits; // 角色个性特征列表
    private String description; // 角色个性描述
    private List<String> interests; // 角色兴趣爱好列表
    private String backgroundStory; // 角色背景故事
    private Short online; // 角色是否在线 0表示离线，1表示在线
    private LocalDateTime createTime; // 角色创建时间，使用时间戳表示
    private LocalDateTime updateTime; // 角色更新时间，使用时间戳表示
}
