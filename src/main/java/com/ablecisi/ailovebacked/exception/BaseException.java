package com.ablecisi.ailovebacked.exception;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.exception <br>
 * 基础异常类，所有业务异常都继承自该类 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/15
 * 星期日
 * 21:10
 **/
public class BaseException extends RuntimeException {
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
}
