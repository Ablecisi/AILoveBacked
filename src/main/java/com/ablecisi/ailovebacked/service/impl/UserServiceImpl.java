package com.ablecisi.ailovebacked.service.impl;

import com.ablecisi.ailovebacked.constant.FollowMessageConstant;
import com.ablecisi.ailovebacked.constant.JwtClaimsConstant;
import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.mapper.PostMapper;
import com.ablecisi.ailovebacked.mapper.UserMapper;
import com.ablecisi.ailovebacked.pojo.dto.UserDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserFollowDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserPasswordChangeDTO;
import com.ablecisi.ailovebacked.pojo.dto.UserProfileUpdateDTO;
import com.ablecisi.ailovebacked.pojo.entity.User;
import com.ablecisi.ailovebacked.pojo.vo.UserVO;
import com.ablecisi.ailovebacked.properties.JwtProperties;
import com.ablecisi.ailovebacked.service.UserService;
import com.ablecisi.ailovebacked.utils.JsonUtil;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PostMapper postMapper;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, PostMapper postMapper, JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
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
        if (!passwordMatches(user, userDTO.getPassword())) {
            throw new BaseException("密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BaseException("账号已停用");
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
        long pc = postMapper.countByUserId(user.getId());
        return UserVO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .name(user.getName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .postCount((int) Math.min(pc, Integer.MAX_VALUE))
                .token(token) // 使用JWT生成
                .postIds(List.of())
                .isFollowed(false) // 登录时不需要关注状态
                .characterIds(List.of())
                .build();
    }

    private boolean passwordMatches(User user, String rawPassword) {
        String stored = user.getPassword();
        if (stored == null) {
            return false;
        }
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, stored);
        }
        if (stored.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userMapper.updateUser(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public FollowMessageConstant followUser(UserFollowDTO userFollowDTO) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException("用户未登录");
        }
        Long authorId = Long.parseLong(userFollowDTO.getAuthorId());
        log.info("用户 {} 执行关注操作，目标作者ID: {}， 是否关注 {}", userId, authorId, userFollowDTO.getIsFollowing());
        // 更新用户关注关系
        if (Boolean.TRUE.equals(userFollowDTO.getIsFollowing())) {
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

    @Override
    public List<String> getCurrentUserInterests() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null || userId <= 0) {
            throw new BaseException("用户未登录");
        }
        return JsonUtil.fromJsonList(userMapper.getUserInterests(userId), String.class);
    }

    @Override
    public UserVO getUserProfile() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null || userId <= 0) {
            throw new BaseException("用户未登录");
        }
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        long pc = postMapper.countByUserId(user.getId());
        return UserVO.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .name(user.getName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .postCount((int) Math.min(pc, Integer.MAX_VALUE))
                .postIds(List.of()) // 这里可以调用帖子服务获取用户的帖子ID列表
                .characterIds(List.of()) // 这里可以调用角色服务获取用户的角色ID列表
                .isFollowed(false) // 自己的资料页，不需要关注状态
                .build();
    }

    @Override
    @Transactional
    public UserVO updateProfile(UserProfileUpdateDTO dto) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null || userId <= 0) {
            throw new BaseException("用户未登录");
        }
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            String nu = dto.getUsername().trim();
            User sameName = userMapper.getUserByUsername(nu);
            if (sameName != null && !sameName.getId().equals(userId)) {
                throw new BaseException("用户名已被占用");
            }
            user.setUsername(nu);
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName().trim());
        }
        if (dto.getDescription() != null) {
            user.setDescription(dto.getDescription());
        }
        if (dto.getAvatarUrl() != null && !dto.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(dto.getAvatarUrl().trim());
        }
        userMapper.updateUser(user);
        return getUserProfile();
    }

    @Override
    @Transactional
    public void changePassword(UserPasswordChangeDTO dto) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null || userId <= 0) {
            throw new BaseException("用户未登录");
        }
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new BaseException("用户不存在");
        }
        verifyOldPassword(user, dto.getOldPassword());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateUser(user);
    }

    private void verifyOldPassword(User user, String rawPassword) {
        if (rawPassword == null) {
            throw new BaseException("请输入原密码");
        }
        String stored = user.getPassword();
        if (stored == null) {
            throw new BaseException("密码未设置");
        }
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            if (!passwordEncoder.matches(rawPassword, stored)) {
                throw new BaseException("原密码错误");
            }
        } else if (!stored.equals(rawPassword)) {
            throw new BaseException("原密码错误");
        }
    }
}
