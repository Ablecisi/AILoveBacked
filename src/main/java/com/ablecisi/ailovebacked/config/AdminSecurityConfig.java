package com.ablecisi.ailovebacked.config;

import com.ablecisi.ailovebacked.admin.AdminAuthenticationEntryPoint;
import com.ablecisi.ailovebacked.admin.AdminJwtAuthFilter;
import com.ablecisi.ailovebacked.properties.AdminJwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(AdminJwtProperties.class)
@RequiredArgsConstructor
public class AdminSecurityConfig {

    private final AdminJwtAuthFilter adminJwtAuthFilter;
    private final AdminAuthenticationEntryPoint adminAuthenticationEntryPoint;

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/admin/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/api/v1/auth/login").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(adminAuthenticationEntryPoint))
                .addFilterBefore(adminJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
