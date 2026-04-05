package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.vo.admin.DauCountRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户按自然日活跃（当日任意一次 App 访问记一条，user_id+date 唯一）
 */
@Mapper
public interface UserActiveDayMapper {

    int upsert(@Param("userId") long userId, @Param("activeDate") LocalDate activeDate);

    List<DauCountRow> countByActiveDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

    long countUsersOnDate(@Param("activeDate") LocalDate activeDate);
}
