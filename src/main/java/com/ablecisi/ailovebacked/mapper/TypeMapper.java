package com.ablecisi.ailovebacked.mapper;

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
 * 19:47
 **/
@Mapper
public interface TypeMapper {
    @Select("SELECT name FROM ailove.type WHERE id = #{typeId}")
    String getTypeNameById(Long typeId);
}
