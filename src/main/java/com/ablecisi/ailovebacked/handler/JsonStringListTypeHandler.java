package com.ablecisi.ailovebacked.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.handler <br>
 * 处理JSON字符串列表的MyBatis类型处理器
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 10:21
 **/
@Component
public class JsonStringListTypeHandler extends BaseTypeHandler<List<String>> {
    private static final ObjectMapper objectMapper = new ObjectMapper(); // Jackson对象映射器

    /**
     * 将字符串列表转换为JSON字符串
     *
     * @param ps        PreparedStatement
     * @param i         参数索引
     * @param parameter 字符串列表参数
     * @param jdbcType  JDBC类型
     * @throws SQLException SQL异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, toJson(parameter));
    }

    /**
     * 获取非空结果集中的字符串列表
     *
     * @param rs         ResultSet
     * @param columnName 列名
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toList(rs.getString(columnName));
    }

    /**
     * 获取非空结果集中的字符串列表
     *
     * @param rs          ResultSet
     * @param columnIndex 列索引
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toList(rs.getString(columnIndex));
    }

    /**
     * 获取存储过程调用中的字符串列表
     *
     * @param cs          CallableStatement
     * @param columnIndex 列索引
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toList(cs.getString(columnIndex));
    }

    /**
     * 将字符串列表转换为JSON字符串
     *
     * @param list 字符串列表
     * @return JSON字符串
     */
    private String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将JSON字符串转换为字符串列表
     *
     * @param json JSON字符串
     * @return 字符串列表
     */
    private List<String> toList(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
