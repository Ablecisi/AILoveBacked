package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * PromptTemplateMapper
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.mapper <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 16:33
 **/
@Mapper
public interface PromptTemplateMapper {
    @Select("SELECT * FROM prompt_template WHERE role_type = #{roleType} LIMIT 1")
    PromptTemplate selectActiveByRole(@Param("roleType") String roleType);
}
