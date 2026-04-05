package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * OpenConversationDTO
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:28
 **/
@Data
public class OpenConversationDTO {
    @NotNull
    private Long characterId;
    /**
     * 可选；为空时用角色名生成默认标题
     */
    private String title;
    /**
     * 可选；会话场景与背景，写入 conversation.scene_background
     */
    private String sceneBackground;
}
