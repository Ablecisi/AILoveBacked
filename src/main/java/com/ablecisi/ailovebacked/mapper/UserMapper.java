package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
     * 根据用户名和密码获取用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户实体
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     */
    @Insert("INSERT INTO ailove.follow_relation (following_id, follower_id, create_time, update_time) VALUES (#{userId}, #{authorId}, NOW(), NOW())")
    Boolean insertFollowRelation(Long userId, Long authorId);

    /**
     * 删除关注关系
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     */
    @Delete("DELETE FROM ailove.follow_relation WHERE following_id = #{userId} AND follower_id = #{authorId}")
    Boolean deleteFollowRelation(Long userId, Long authorId);

    /**
     * 查询用户是否关注作者
     *
     * @param userId   用户ID
     * @param authorId 作者ID
     * @return 是否关注
     */
    @Select("SELECT COUNT(*) > 0 FROM ailove.follow_relation WHERE following_id = #{userId} AND follower_id = #{authorId}")
    Boolean selectFollowRelation(Long userId, Long authorId);
}
