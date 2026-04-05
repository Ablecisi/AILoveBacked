package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.TypeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppTypeMapper {

    List<TypeItem> selectAll();

    TypeItem selectById(@Param("id") Long id);

    int insert(TypeItem row);

    int updateRow(TypeItem row);

    int deleteById(@Param("id") Long id);

    long countAll();
}
