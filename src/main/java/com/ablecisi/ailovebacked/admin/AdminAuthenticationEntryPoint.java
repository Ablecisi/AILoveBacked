package com.ablecisi.ailovebacked.admin;

import com.ablecisi.ailovebacked.constant.MessageConstant;
import com.ablecisi.ailovebacked.constant.StatusCodeConstant;
import com.ablecisi.ailovebacked.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AdminAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Object> body = Result.error(StatusCodeConstant.TOKEN_EXPIRED, MessageConstant.USER_NOT_LOGIN, null);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
