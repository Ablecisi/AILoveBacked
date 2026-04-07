package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.entity.AppConfigRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppBootstrapService {

    private final AppConfigMapper appConfigMapper;

    /**
     * 不向 C 端 bootstrap 暴露的敏感键（例如 LLM API Key）。
     */
    private static final Set<String> BOOTSTRAP_EXCLUDED_KEYS = Set.of("llm.apiKey");

    /**
     * C 端 {@code GET /api/app/bootstrap}：合并默认值与库内配置，并过滤敏感键。
     */
    public Map<String, String> buildBootstrap() {
        return buildBootstrapInternal(true);
    }

    /**
     * 管理端「App 配置」全量列表：包含库内所有键（含 {@code llm.apiKey}），便于排查。
     */
    public Map<String, String> buildBootstrapForAdmin() {
        return buildBootstrapInternal(false);
    }

    private Map<String, String> buildBootstrapInternal(boolean excludeSensitive) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("bootstrap.version", "1");
        map.put("minAppVersion", "1");
        map.put("home.featured.enabled", "true");
        map.put("rateLimit.llm.perMinute", "30");
        map.put("rateLimit.emotion.perMinute", "120");
        map.put("rateLimit.llm.dailyQuota", "0");
        for (AppConfigRow row : appConfigMapper.selectAll()) {
            if (row.getConfigKey() != null && row.getConfigValue() != null) {
                if (excludeSensitive && BOOTSTRAP_EXCLUDED_KEYS.contains(row.getConfigKey())) {
                    continue;
                }
                map.put(row.getConfigKey(), row.getConfigValue());
            }
        }
        return map;
    }
}
