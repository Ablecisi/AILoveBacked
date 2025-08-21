package com.ablecisi.ailovebacked.utils;

import com.alibaba.fastjson2.JSON;

import java.lang.reflect.Type;
import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.utils <br>
 * JSON工具类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 19:52
 **/
public class JsonUtil {

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串
     */
    public static <T> String toJson(T obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类型的Class对象
     * @param <T>   目标类型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将JSON字符串转换为指定类型的对象，如果转换失败则返回默认值
     *
     * @param json         JSON字符串
     * @param clazz        目标类型的Class对象
     * @param defaultValue 默认值
     * @param <T>          目标类型
     * @return 转换后的对象或默认值
     */
    public static <T> T fromJson(String json, Class<T> clazz, T defaultValue) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     *
     * @param json JSON字符串
     * @param type 目标类型的Type对象
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    /**
     * 将JSON字符串转换为指定类型的对象列表
     *
     * @param json  JSON字符串
     * @param clazz 目标类型的Class对象
     * @param <T>   目标类型
     * @return 转换后的对象列表
     */
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }
}
