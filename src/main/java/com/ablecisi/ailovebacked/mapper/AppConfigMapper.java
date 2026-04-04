package com.ablecisi.ailovebacked.mapper;

import com.ablecisi.ailovebacked.pojo.entity.AppConfigRow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppConfigMapper {

    @Select("SELECT config_key AS configKey, config_value AS configValue FROM app_config")
    List<AppConfigRow> selectAll();

    @Insert("INSERT INTO app_config (config_key, config_value) VALUES (#{key}, #{value}) "
            + "ON DUPLICATE KEY UPDATE config_value = #{value}, update_time = CURRENT_TIMESTAMP")
    int upsert(@Param("key") String key, @Param("value") String value);

    @Select("SELECT config_value FROM app_config WHERE config_key = #{key} LIMIT 1")
    String selectValueByKey(@Param("key") String key);
}
