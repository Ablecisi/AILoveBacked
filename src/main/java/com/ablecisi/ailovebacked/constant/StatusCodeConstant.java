package com.ablecisi.ailovebacked.constant;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.constant <br>
 * 状态码常量类，定义了系统中使用的各种状态码 <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/6/16
 * 星期一
 * 20:47
 **/
public class StatusCodeConstant {
    public static final int SUCCESS = 1; // 登录成功
    public static final int FAILURE = 0; // 登录失败
    public static final int NETWORK_ERROR = 500; // 网络错误
    public static final int INVALID_CREDENTIALS = 403; // 无效的凭证
    public static final int NOT_FOUND = 404; // 用户未找到
    public static final int SERVER_ERROR = 500; // 服务器错误
    public static final int TOKEN_EXPIRED = 401; // Token过期
}
