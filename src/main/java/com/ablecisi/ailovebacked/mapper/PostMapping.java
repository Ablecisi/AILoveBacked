package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 02:21
 **/
@Mapper
public interface PostMapping {

    @Select("SELECT * FROM ailove.post WHERE user_id = #{id}")
    List<Post> getPostsByUserId(Long id);
}
