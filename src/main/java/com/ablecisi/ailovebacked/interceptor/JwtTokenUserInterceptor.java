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

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
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

    private void writeUnauthorized(HttpServletResponse response, int bodyCode, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Object> body = Result.error(bodyCode, message, null);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
