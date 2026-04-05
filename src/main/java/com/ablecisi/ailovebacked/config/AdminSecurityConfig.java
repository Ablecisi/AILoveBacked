package com.ablecisi.ailovebacked.config;

import com.ablecisi.ailovebacked.properties.AdminJwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 仅关闭 Spring Security 对 URL 的默认拦截；管理端鉴权由 {@link com.ablecisi.ailovebacked.interceptor.JwtTokenAdminInterceptor} 负责，
 * C 端由 {@link com.ablecisi.ailovebacked.interceptor.JwtTokenUserInterceptor} 负责，二者路径互不重叠。
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AdminJwtProperties.class)
public class AdminSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
