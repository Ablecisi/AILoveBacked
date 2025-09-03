package com.ablecisi.ailovebacked.result;

import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.result
 * Result <br>
 *
 * @author Ablecisi
 * @version 1.0
 * 2025/4/25
 * 星期五
 * 22:51
 */
@Data
public class Result<T> implements Serializable {
    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.msg = "请求成功";
        result.code = StatusCodeConstant.SUCCESS;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.msg = "请求成功";
        result.code = StatusCodeConstant.SUCCESS;
        return result;
    }

    public static <T> Result<T> success(String msg, T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.msg = msg;
        result.code = StatusCodeConstant.SUCCESS;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = StatusCodeConstant.FAILURE;
        return result;
    }

    public static <T> Result<T> error(String msg, T object) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.data = object;
        result.code = StatusCodeConstant.FAILURE;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg, T object) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        result.data = object;
        return result;
    }

}
