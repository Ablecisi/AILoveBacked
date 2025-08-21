package com.ablecisi.ailovebacked.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.pojo.dto <br>
 * UserFollowDTO <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 11:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowDTO implements Serializable {
    private String userId; // 用户ID
    private String authorId; // 作者ID
    private Boolean isFollowing; // 是否关注
}
