package com.ablecisi.ailovebacked.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ailove.admin")
public class AdminJwtProperties {
    /**
     * 管理端 JWT 签名密钥（与 C 端 ailove.jwt.user-secret-key 独立）
     */
    private String jwtSecretKey = "";
    private long ttlMs = 86400000L;
}
