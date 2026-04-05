package com.ablecisi.ailovebacked.interceptor;

import com.ablecisi.ailovebacked.constant.JwtClaimsConstant;
import com.ablecisi.ailovebacked.constant.MessageConstant;
import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.context.BaseContext;
import com.ablecisi.ailovebacked.properties.JwtProperties;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * jwt令牌校验的拦截器（失败时 HTTP 401 + JSON，body.code 使用 TOKEN_EXPIRED 等以兼容客户端）
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;

    public JwtTokenUserInterceptor(JwtProperties jwtProperties, ObjectMapper objectMapper) {
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
    }

    /**
     * User 端 jwt 令牌校验
     *
     * @param request  请求
     * @param response 响应
     * @param handler  方法
     * @return 拦截结果
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (isAnonymousUserPath(request)) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            log.debug("非 Controller 方法，放行");
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());

        try {
            if (token == null || token.isEmpty()) {
                writeUnauthorized(response, StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN);
                return false;
            }

            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Date expiration = claims.getExpiration();
            if (expiration == null || expiration.before(new Date())) {
                writeUnauthorized(response, StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN);
                return false;
            }

            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            log.debug("jwt 校验通过 userId={}", userId);
            return true;
        } catch (Exception ex) {
            log.warn("jwt 校验失败: {}", ex.getMessage());
            try {
                writeUnauthorized(response, StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN);
            } catch (Exception e) {
                log.error("写入 401 响应失败", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return false;
        }
    }

    /**
     * C 端无需登录的接口（与 {@code WebMvcConfiguration} 中 excludePathPatterns 保持一致）。
     * 单独判断可避免部分环境下路径匹配与 exclude 不一致导致登录仍被拦截（如尾随斜杠、网关改写 URI 等）。
     */
    private boolean isAnonymousUserPath(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = normalizedPathWithinContext(request);
        return "/api/user/login".equals(path)
                || "/api/app/bootstrap".equals(path)
                || "/api/post/feed".equals(path);
    }

    /**
     * 去掉 context-path 与末尾斜杠，便于白名单比对
     */
    private static String normalizedPathWithinContext(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && uri != null && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());
        }
        if (uri == null || uri.isEmpty()) {
            return "/";
        }
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    /**
     * 401 响应
     */
    private void writeUnauthorized(HttpServletResponse response, int bodyCode, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Object> body = Result.error(bodyCode, message, null);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
