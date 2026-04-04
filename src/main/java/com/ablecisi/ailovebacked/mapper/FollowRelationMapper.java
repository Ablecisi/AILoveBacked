package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.FollowRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowRelationMapper {

    FollowRelation selectById(@Param("id") Long id);

    long countAll();

    List<FollowRelation> selectPage(@Param("offset") int offset, @Param("size") int size);

    int insert(FollowRelation row);

    int updateRow(FollowRelation row);

    int deleteById(@Param("id") Long id);
}
