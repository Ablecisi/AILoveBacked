package com.ablecisi.ailovebacked.admin;

import com.ablecisi.ailovebacked.constant.MessageConstant;
import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.properties.AdminJwtProperties;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AdminJwtAuthFilter extends OncePerRequestFilter {

    private final AdminJwtProperties adminJwtProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String path = request.getRequestURI();
        return path.endsWith("/admin/api/v1/auth/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            writeUnauthorized(response);
            return;
        }
        String token = header.substring(7).trim();
        try {
            Claims claims = JwtUtil.parseJWT(adminJwtProperties.getJwtSecretKey(), token);
            String username = claims.getSubject();
            if (username == null || username.isEmpty()) {
                writeUnauthorized(response);
                return;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                Result.error(StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN, null));
    }
}
