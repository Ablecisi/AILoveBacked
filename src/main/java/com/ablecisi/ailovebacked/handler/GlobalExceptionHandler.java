package com.ablecisi.ailovebacked.handler;

import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.exception.TokenObsoleteException;
import com.ablecisi.ailovebacked.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.handler <br>
 * 全局异常处理器，处理项目中抛出的业务异常
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 21:09
 **/
@RestControllerAdvice // 该注解用于定义统一的异常处理类，可以用于捕获Controller层抛出的异常
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     *
     * @param ex 业务异常
     * @return 返回异常信息
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(404, ex.getMessage(), null);
    }

    /**
     * 捕获业务异常
     *
     * @param ex 业务异常
     * @return 返回异常信息
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(TokenObsoleteException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(StatusCodeConstant.INVALID_CREDENTIALS, ex.getMessage(), null);
    }


    /**
     * 捕获SQL异常
     *
     * @param ex SQL异常
     * @return 返回异常信息
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL异常：{}", ex.getMessage());
        String message = ex.getMessage();
        if (message != null && !message.isEmpty()) {
            return Result.error(500, message, null);
        }
        return Result.error(500, "未知SQL错误", null);
    }

}
