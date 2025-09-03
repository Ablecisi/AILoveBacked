package com.ablecisi.ailovebacked.handler;

import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.exception.TokenObsoleteException;
import com.ablecisi.ailovebacked.result.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        log.error("Base异常信息：{}", ex.getMessage());
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
        log.error("Token异常信息：{}", ex.getMessage());
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
            return Result.error(StatusCodeConstant.SQL_ERROR, message, null);
        }
        return Result.error(StatusCodeConstant.SQL_ERROR, "未知SQL错误", null);
    }

    /**
     * 非法参数异常
     *
     * @param e 非法参数异常
     * @return 返回异常信息
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgument(IllegalArgumentException e) {
        log.error("非法参数异常：{}", e.getMessage());
        return Result.error(StatusCodeConstant.UNPROCESSABLE_ENTITY, "非法参数异常: " + e.getMessage(), null);
    }

    /**
     * 捕获参数不能为null异常
     *
     * @param e 参数不能为null异常
     * @return 返回异常信息
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<?> handleValidation(Exception e) {
        log.error("参数校验异常：{}", e.getMessage());
        String msg;
        if (e instanceof MethodArgumentNotValidException manve) {
            msg = manve.getBindingResult().getFieldErrors().stream()
                    .map(fe -> fe.getField() + ":" + fe.getDefaultMessage()).findFirst().orElse("参数校验失败");
        } else if (e instanceof BindException be) {
            msg = be.getBindingResult().getFieldErrors().stream()
                    .map(fe -> fe.getField() + ":" + fe.getDefaultMessage()).findFirst().orElse("参数绑定失败");
        } else {
            msg = "参数错误";
        }
        return Result.error(StatusCodeConstant.UNPROCESSABLE_ENTITY, msg, null);
    }

    /**
     * 捕获参数约束异常
     *
     * @param e 参数约束异常
     * @return 返回异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraint(ConstraintViolationException e) {
        log.error("参数约束异常：{}", e.getMessage());
        String msg = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ":" + v.getMessage()).findFirst().orElse("参数不合法");
        return Result.error(msg);
    }

    /**
     * 捕获请求体解析异常
     *
     * @param e 请求体解析异常
     * @return 返回异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleReadable(HttpMessageNotReadableException e) {
        log.error("请求体解析异常：{}", e.getMessage());
        return Result.error(StatusCodeConstant.INTERNAL_SERVER_ERROR, "请求体解析失败", null);
    }

    /**
     * 捕获其他异常
     *
     * @param e 其他异常
     * @return 返回异常信息
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleOther(Exception e) {
        log.error("系统异常：", e);
        // 生产上可记录日志 e
        return Result.error(StatusCodeConstant.INTERNAL_SERVER_ERROR, "服务器开小差啦", null);
    }

}
