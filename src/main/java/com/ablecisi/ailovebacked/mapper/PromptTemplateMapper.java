package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.PromptTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromptTemplateMapper {

    PromptTemplate selectActiveByRole(@Param("roleType") String roleType);

    List<PromptTemplate> listAllForAdmin();

    PromptTemplate selectById(@Param("id") Long id);

    int insert(PromptTemplate row);

    int updateById(PromptTemplate row);
}
