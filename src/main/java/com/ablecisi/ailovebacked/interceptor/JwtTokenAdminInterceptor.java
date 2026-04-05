package com.ablecisi.ailovebacked.interceptor;

import com.ablecisi.ailovebacked.constant.MessageConstant;
import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.properties.AdminJwtProperties;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 管理端 JWT 校验（与 {@link JwtTokenUserInterceptor} 同属 MVC 拦截器，只作用于 /admin/api/**，不影响 C 端 /api/**）。
 * 令牌：Authorization: Bearer &lt;token&gt;，密钥见 ailove.admin.jwt-secret-key。
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    private final AdminJwtProperties adminJwtProperties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (isAnonymousAdminPath(request)) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if (header == null || !header.startsWith("Bearer ")) {
                writeUnauthorized(response);
                return false;
            }
            String token = header.substring(7).trim();
            Claims claims = JwtUtil.parseJWT(adminJwtProperties.getJwtSecretKey(), token);
            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.before(new Date())) {
                writeUnauthorized(response);
                return false;
            }
            String subject = claims.getSubject();
            if (subject == null || subject.isEmpty()) {
                writeUnauthorized(response);
                return false;
            }
            log.debug("admin jwt ok, sub={}", subject);
            return true;
        } catch (Exception ex) {
            log.warn("admin jwt 校验失败: {}", ex.getMessage());
            try {
                writeUnauthorized(response);
            } catch (Exception e) {
                log.error("写入 401 响应失败", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return false;
        }
    }

    private boolean isAnonymousAdminPath(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = normalizedPathWithinContext(request);
        return "/admin/api/v1/auth/login".equals(path);
    }

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

    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Object> body = Result.error(StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN, null);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
