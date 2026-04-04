package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.EmotionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmotionLogMapper {

    EmotionLog selectById(@Param("id") Long id);

    long countAll();

    List<EmotionLog> selectPage(@Param("offset") int offset, @Param("size") int size);

    int insert(EmotionLog row);

    int updateRow(EmotionLog row);

    int deleteById(@Param("id") Long id);
}
