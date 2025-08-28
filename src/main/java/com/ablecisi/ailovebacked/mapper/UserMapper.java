package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:32
 **/
@Mapper
public interface UserMapper {
    /**
     * 根据用户ID获取用户名
     *
     * @param userId 用户ID
     * @return 用户名
     */
    User getUserById(Long userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户实体
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     */
    @Insert("INSERT INTO follow_relation (following_id, follower_id, create_time, update_time) VALUES (#{userId}, #{authorId}, NOW(), NOW())")
    Boolean insertFollowRelation(Long userId, Long authorId);

    /**
     * 删除关注关系
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     */
    @Delete("DELETE FROM follow_relation WHERE following_id = #{userId} AND follower_id = #{authorId}")
    Boolean deleteFollowRelation(Long userId, Long authorId);

    /**
     * 查询用户是否关注作者
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     * @return 是否关注
     */
    @Select("SELECT COUNT(*) > 0 FROM follow_relation WHERE following_id = #{userId} AND follower_id = #{authorId}")
    Boolean selectFollowRelation(Long userId, Long authorId);

    /**
     * 根据用户ID获取用户创建的AI角色ID列表
     *
     * @param userId 用户ID
     * @return AI角色ID列表
     */
    @Select("SELECT id from ai_character WHERE user_id = #{userId}")
    List<Long> getCharacterIdsByUserId(Long userId);

    /**
     * 更新用户信息
     *
     * @param user 用户实体
     * @return 是否更新成功
     */
    Boolean updateUser(User user);

    /**
     * 根据用户ID获取用户兴趣标签
     *
     * @param userId 用户ID
     * @return 用户兴趣标签列表
     */
    String getUserInterests(Long userId);
}
