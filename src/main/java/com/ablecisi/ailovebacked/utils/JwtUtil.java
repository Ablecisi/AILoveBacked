package com.ablecisi.ailovebacked.utils;

import com.ablecisi.ailovebacked.exception.TokenObsoleteException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具（jjwt 0.12+）。对任意长度 secret 做 SHA-256 派生为 HMAC-SHA256 密钥（与旧版「短字符串直接作 key」不兼容，升级后需重新登录）。
 */
public final class JwtUtil {

    private JwtUtil() {
    }

    private static SecretKey deriveKey(String secret) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(secret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        SecretKey key = deriveKey(secretKey);
        Date exp = new Date(System.currentTimeMillis() + ttlMillis);
        return Jwts.builder()
                .claims(claims)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public static Claims parseJWT(String secretKey, String token) {
        try {
            SecretKey key = deriveKey(secretKey);
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new TokenObsoleteException(e.getMessage());
        }
    }
}
