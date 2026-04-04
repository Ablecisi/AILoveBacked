package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.AdminUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT COUNT(*) FROM admin_user")
    long count();

    @Select("SELECT * FROM admin_user WHERE username = #{username}")
    AdminUser selectByUsername(String username);

    @Select("SELECT * FROM admin_user WHERE id = #{id}")
    AdminUser selectById(@Param("id") Long id);

    @Select("SELECT * FROM admin_user ORDER BY id ASC")
    List<AdminUser> selectAll();

    @Insert("INSERT INTO admin_user (username, password, create_time, update_time) "
            + "VALUES (#{username}, #{password}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AdminUser user);

    @Update("UPDATE admin_user SET username = #{username}, update_time = NOW() WHERE id = #{id}")
    int updateUsername(@Param("id") Long id, @Param("username") String username);

    @Update("UPDATE admin_user SET password = #{password}, update_time = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM admin_user WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
