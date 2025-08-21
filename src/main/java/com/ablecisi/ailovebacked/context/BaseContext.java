package com.ablecisi.ailovebacked.context;

/**
 * sky-take-out
 * com.sky.context
 * 用于存储当前线程的用户id<br>
 * {@code 时间: 2025/3/27 星期四 18:02}
 *
 * @author Ablecisi
 * @version 1.0
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
