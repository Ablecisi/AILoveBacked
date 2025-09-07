package com.ablecisi.ailovebacked.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    private Long id;                    // 角色ID
    private Long userId;                // 角色所属用户ID
    private String name;                // 角色名称
    private Long typeId;                // 角色类型，例如：AI助手、虚拟偶像等
    private Integer gender;             // 角色性别，0表示男性，1表示女性，2表示其他 / 0男 1女 2其他/未知
    private Integer age;                // 角色年龄，单位为岁
    private String imageUrl;            // 角色头像URL
    private String traits;              // 角色个性特征列表 / JSON 或 逗号分隔，后端不强制解析
    private String personaDesc;         // 角色个性描述
    private String interests;           // 角色兴趣爱好列表 / JSON 或 逗号分隔
    private String backstory;           // 角色背景故事
    private Integer online;             // 角色是否在线 0表示离线，1表示在线/ 1/0
    private Integer status;             // 角色状态，0表示不可用，1表示可用 / 1启用 0下线
    private LocalDateTime createTime;   // 角色创建时间，使用时间戳表示
    private LocalDateTime updateTime;   // 角色更新时间，使用时间戳表示
}
