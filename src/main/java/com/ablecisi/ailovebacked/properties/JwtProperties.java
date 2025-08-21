package com.ablecisi.ailovebacked.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt配置类
 */
@Component
@ConfigurationProperties(prefix = "ailove.jwt")
@Data
public class JwtProperties {
    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey; // 用户端用户生成jwt令牌的密钥
    private long userTtl; // 用户端用户生成jwt令牌的有效时间
    private String userTokenName; // 用户端用户生成jwt令牌的名称

}
