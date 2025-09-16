package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.mapper.UserProfileMapper;
import com.ablecisi.ailovebacked.pojo.entity.UserProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProfileService
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:43
 **/
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserProfileMapper mapper;

    public String updateAndSummarize(Long userId, String emotion) {
        UserProfile p = mapper.selectByUserId(userId);
        if (p == null) {
            p = new UserProfile();
            p.setUserId(userId);
            p.setEmotionStats("{}");
            mapper.insert(p);
        }
        Map<String, Integer> stats = parse(p.getEmotionStats());
        stats.put(emotion, stats.getOrDefault(emotion, 0) + 1);
        p.setEmotionStats(toJson(stats));
        mapper.update(p);
        return toBrief(stats);
    }

    private Map<String, Integer> parse(String json) {
        if (json == null || json.isBlank()) return new HashMap<>();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Integer>>() {
            });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private String toJson(Map<String, Integer> m) {
        try {
            return new ObjectMapper().writeValueAsString(m);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String toBrief(Map<String, Integer> m) {
        int sum = m.values().stream().mapToInt(i -> i).sum();
        if (sum == 0) return "暂无画像";
        return m.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .map(e -> e.getKey() + " " + (int) Math.round(100.0 * e.getValue() / sum) + "%")
                .collect(Collectors.joining("，近况："));
    }
}

