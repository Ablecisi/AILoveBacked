package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * UserProfileMapper
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:32
 **/
@Mapper
public interface UserProfileMapper {
    UserProfile selectByUserId(@Param("userId") Long userId);

    int insert(UserProfile po);

    int update(UserProfile po);
}

