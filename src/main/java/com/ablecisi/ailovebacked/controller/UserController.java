package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.constant.FollowMessageConstant;
import com.ablecisi.ailovebacked.pojo.dto.UserDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserFollowDTO;
import com.ablecisi.ailovebacked.pojo.vo.UserVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.controller
 * UserController <br>
 *
 * @author Ablecisi
 * @version 1.0
 * 2025/4/25
 * 星期五
 * 22:50
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param userDTO 用户数据传输对象
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserDTO userDTO) {
        log.info("用户{}正在登录...", userDTO.getUsername());
        UserVO userVO = userService.login(userDTO);

        return Result.success("登录成功！", userVO);
    }

    /**
     * 用户关注或取消关注
     *
     * @param userFollowDTO 用户关注数据传输对象
     * @return 关注结果
     */
    @PostMapping("/follow")
    public Result<Boolean> followUser(@RequestBody UserFollowDTO userFollowDTO) {
        log.info("用户 {} 关注作者 {}", userFollowDTO.getUserId(), userFollowDTO.getAuthorId());
        if (userFollowDTO.getUserId() == null || userFollowDTO.getAuthorId() == null) {
            log.warn("用户ID或作者ID不能为空.");
            return Result.error("用户ID或作者ID不能为空");
        }
        FollowMessageConstant isFollowing = userService.followUser(userFollowDTO);
        if (isFollowing == FollowMessageConstant.FOLLOW_SUCCESS) {
            log.info("用户 {} 关注作者 {} 成功", userFollowDTO.getUserId(), userFollowDTO.getAuthorId());
            return Result.success(FollowMessageConstant.FOLLOW_SUCCESS.getMessage(), true);
        } else if (isFollowing == FollowMessageConstant.UNFOLLOW_SUCCESS) {
            log.info("用户 {} 取消关注作者 {} 成功", userFollowDTO.getUserId(), userFollowDTO.getAuthorId());
            return Result.success(FollowMessageConstant.UNFOLLOW_SUCCESS.getMessage(), false);
        } else {
            log.error("关注或取消关注失败: {}", isFollowing.getMessage());
            return Result.error(isFollowing.getMessage());
        }
    }

    /**
     * 查询用户是否关注作者
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     * @return 是否关注
     */
    @GetMapping("/follow")
    public Result<Boolean> isFollowing(@RequestParam("userId") String userId, @RequestParam("authorId") String authorId) {
        log.info("查询用户 {} 是否关注作者 {}", userId, authorId);
        if (userId == null || authorId == null) {
            log.warn("用户ID或作者ID不能为空");
            return Result.error("用户ID或作者ID不能为空");
        }
        Boolean isFollowing = userService.isFollowing(Long.parseLong(userId), Long.parseLong(authorId));
        if (isFollowing != null) {
            return Result.success("查询成功", isFollowing);
        } else {
            return Result.error("查询失败，可能是用户或作者不存在");
        }
    }

    /**
     * 获取用户兴趣标签
     *
     * @param userId 用户ID
     * @return 兴趣标签列表
     */
    @GetMapping("/interests")
    public Result<List<String>> getUserInterests(String userId) {
        log.info("获取用户 {} 的兴趣标签", userId);
        if (userId == null) {
            log.warn("用户ID不能为空");
            return Result.error("用户ID不能为空");
        }
        List<String> interests = userService.getUserInterests(Long.parseLong(userId));
        if (interests != null) {
            return Result.success("获取成功", interests);
        } else {
            return Result.error("获取失败，可能是用户不存在");
        }
    }

    @GetMapping("/profile")
    public Result<UserVO> getUserProfile() {
        log.info("获取用户个人信息");
        UserVO userVO = userService.getUserProfile();
        return Result.success("获取成功", userVO);
    }
}
