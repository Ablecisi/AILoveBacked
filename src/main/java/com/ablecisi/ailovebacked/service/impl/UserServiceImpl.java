package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.constant.FollowMessageConstant;
import com.ablecisi.ailovebacked.constant.JwtClaimsConstant;
import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.dto.UserDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserFollowDTO;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.UserVO;
import com.ablecisi.ailovebacked.properties.JwtProperties;
import com.ablecisi.ailovebacked.service.UserService;
import com.ablecisi.ailovebacked.utils.JsonUtil;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service.impl <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:32
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, JwtProperties jwtProperties) {
        this.userMapper = userMapper;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @Transactional
    public UserVO login(UserDTO userDTO) {
        // 根据用户ID获取用户信息
        User user = userMapper.getUserByUsername(userDTO.getUsername());

        // 如果用户不存在，返回null
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        // 检查密码是否匹配
        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new BaseException("密码错误");
        }
        BaseContext.setCurrentId(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        log.info("用户 {} 登录成功，生成的token: {}", user.getUsername(), token);

        // 创建用户视图对象
        return UserVO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .token(token) // 使用JWT生成
                .postIds(List.of())
                .isFollowed(false) // 登录时不需要关注状态
                .characterIds(List.of())
                .build();
    }

    @Override
    @Transactional
    public FollowMessageConstant followUser(UserFollowDTO userFollowDTO) {
        Long userId = Long.parseLong(userFollowDTO.getUserId());
        Long authorId = Long.parseLong(userFollowDTO.getAuthorId());
        log.info("用户 {} 执行关注操作，目标作者ID: {}， 是否关注 {}", userId, authorId, userFollowDTO.getIsFollowing());
        // 更新用户关注关系
        if (userFollowDTO.getIsFollowing()) {
            // 如果是关注操作，添加关注关系
            // 首先检查是否已经关注
            if (userMapper.selectFollowRelation(userId, authorId)) {
                // 如果已经关注，返回false
                return FollowMessageConstant.FOLLOW_ALREADY_EXISTS;
            }
            if (userMapper.insertFollowRelation(userId, authorId)) {
                // 更新作者的粉丝数
                User author = userMapper.getUserById(authorId);
                author.setFollowersCount(author.getFollowersCount() + 1);
                userMapper.updateUser(author);
                User user = userMapper.getUserById(userId);
                user.setFollowingCount(user.getFollowingCount() + 1);
                userMapper.updateUser(user);
                return FollowMessageConstant.FOLLOW_SUCCESS;
            }

            return FollowMessageConstant.FOLLOW_ERROR;
        } else {
            // 如果是取消关注操作，删除关注关系
            if (userMapper.deleteFollowRelation(userId, authorId)) {
                // 更新作者的粉丝数
                User author = userMapper.getUserById(authorId);
                author.setFollowersCount(author.getFollowersCount() - 1);
                userMapper.updateUser(author);
                User user = userMapper.getUserById(userId);
                user.setFollowingCount(user.getFollowingCount() - 1);
                userMapper.updateUser(user);

                return FollowMessageConstant.UNFOLLOW_SUCCESS;
            }

            return FollowMessageConstant.UNFOLLOW_ERROR;
        }
    }

    @Override
    public Boolean isFollowing(Long userId, Long authorId) {
        // 检查用户是否关注作者
        if (userId == null || authorId == null) {
            return false; // 用户ID或作者ID不能为空
        }
        return userMapper.selectFollowRelation(userId, authorId);
    }

    /**
     * 根据用户ID获取用户兴趣标签
     *
     * @param userId 用户ID
     * @return 用户兴趣标签列表
     */
    @Override
    public List<String> getUserInterests(Long userId) {
        List<String> interests = JsonUtil.fromJsonList(userMapper.getUserInterests(userId), String.class);
        System.out.println(interests);
        return interests;
    }
}
