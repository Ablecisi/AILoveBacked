package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.dto.AiCharacterQueryDTO;
import com.ablecisi.ailovebacked.pojo.entity.AiCharacter;
import com.ablecisi.ailovebacked.pojo.vo.AiCharacterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/22
 * 星期五
 * 20:38
 **/
@Mapper
public interface AiCharacterMapper {

    // CUD 仍用实体
    int insert(AiCharacter po);

    int update(AiCharacter po);

    int updateOnline(@Param("id") Long id, @Param("online") Integer online);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(@Param("id") Long id);

    // 查询直接返回 VO（已带 typeName）
    AiCharacterVO selectById(@Param("id") Long id);

    List<AiCharacterVO> pageQuery(@Param("q") AiCharacterQueryDTO q);

    long pageCount(@Param("q") AiCharacterQueryDTO q);

    List<String> getCharacterTypes();
}
