package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.exception.RateLimitExceededException;
import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiRateLimiter {

    private static final ZoneId CN = ZoneId.of("Asia/Shanghai");

    private final StringRedisTemplate stringRedisTemplate;
    private final AppConfigMapper appConfigMapper;

    public void assertChatSendAllowed(Long userId) {
        if (userId == null) {
            return;
        }
        assertMinute("ailove:rl:emo:", userId,
                parsePositiveInt(configOrDefault("rateLimit.emotion.perMinute", "120"), 120), "情绪分析");
        assertMinute("ailove:rl:llm:", userId,
                parsePositiveInt(configOrDefault("rateLimit.llm.perMinute", "30"), 30), "AI 回复");
        assertDailyLlmQuota(userId);
    }

    private void assertMinute(String prefix, Long userId, int maxPerMinute, String label) {
        if (maxPerMinute <= 0) {
            return;
        }
        long minute = System.currentTimeMillis() / 60_000L;
        String key = prefix + userId + ":" + minute;
        try {
            Long n = stringRedisTemplate.opsForValue().increment(key);
            if (n != null && n == 1) {
                stringRedisTemplate.expire(key, Duration.ofSeconds(120));
            }
            if (n != null && n > maxPerMinute) {
                throw new RateLimitExceededException(label + "请求过于频繁，请稍后再试");
            }
        } catch (RateLimitExceededException e) {
            throw e;
        } catch (Exception e) {
            log.warn("rate limit skipped (redis): {}", e.getMessage());
        }
    }

    private void assertDailyLlmQuota(Long userId) {
        int daily = parsePositiveInt(configOrDefault("rateLimit.llm.dailyQuota", "0"), 0);
        if (daily <= 0) {
            return;
        }
        String day = LocalDate.now(CN).format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = "ailove:rl:llm:day:" + userId + ":" + day;
        try {
            Long n = stringRedisTemplate.opsForValue().increment(key);
            if (n != null && n == 1) {
                stringRedisTemplate.expire(key, Duration.ofHours(48));
            }
            if (n != null && n > daily) {
                throw new RateLimitExceededException("今日 AI 对话次数已达上限，请明日再试");
            }
        } catch (RateLimitExceededException e) {
            throw e;
        } catch (Exception e) {
            log.warn("daily quota skipped (redis): {}", e.getMessage());
        }
    }

    private String configOrDefault(String key, String defaultValue) {
        try {
            String v = appConfigMapper.selectValueByKey(key);
            if (v != null && !v.isBlank()) {
                return v.trim();
            }
        } catch (Exception e) {
            log.debug("config {}: {}", key, e.getMessage());
        }
        return defaultValue;
    }

    private static int parsePositiveInt(String raw, int defaultValue) {
        try {
            int v = Integer.parseInt(raw.trim());
            return v >= 0 ? v : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
