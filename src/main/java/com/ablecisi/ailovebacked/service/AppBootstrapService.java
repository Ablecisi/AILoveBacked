package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.AppConfigMapper;
import com.ablecisi.ailovebacked.pojo.entity.AppConfigRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppBootstrapService {

    private final AppConfigMapper appConfigMapper;

    /**
     * 合并默认值与库内配置（库覆盖默认）。值均为字符串，客户端可自行解析 JSON。
     */
    public Map<String, String> buildBootstrap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("bootstrap.version", "1");
        map.put("minAppVersion", "1");
        map.put("home.featured.enabled", "true");
        map.put("rateLimit.llm.perMinute", "30");
        map.put("rateLimit.emotion.perMinute", "120");
        map.put("rateLimit.llm.dailyQuota", "0");
        for (AppConfigRow row : appConfigMapper.selectAll()) {
            if (row.getConfigKey() != null && row.getConfigValue() != null) {
                map.put(row.getConfigKey(), row.getConfigValue());
            }
        }
        return map;
    }
}
