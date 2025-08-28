package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.constant.FollowMessageConstant;
import com.ablecisi.ailovebacked.pojo.dto.UserDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserFollowDTO;
import com.ablecisi.ailovebacked.pojo.vo.UserVO;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:32
 **/
public interface UserService {
    /**
     * 用户登录
     *
     * @param userDTO 用户数据传输对象
     * @return 用户视图对象
     */
    UserVO login(UserDTO userDTO);

    /**
     * 用户关注或取消关注
     *
     * @param userFollowDTO 用户关注数据传输对象
     * @return 是否成功关注或取消关注
     */
    FollowMessageConstant followUser(UserFollowDTO userFollowDTO);

    /**
     * 根据用户ID获取用户名
     *
     * @param userId 用户ID
     * @return 用户名
     */
    Boolean isFollowing(Long userId, Long authorId);

    /**
     * 根据用户ID获取用户兴趣标签
     *
     * @param userId 用户ID
     * @return 用户兴趣标签列表
     */
    List<String> getUserInterests(Long userId);
}
