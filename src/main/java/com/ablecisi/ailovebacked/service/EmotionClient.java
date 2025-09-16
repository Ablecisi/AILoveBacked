package com.ablecisi.ailovebacked.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * EmotionClient
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.service <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/10
 * 星期三
 * 17:36
 **/
@Service
@RequiredArgsConstructor
public class EmotionClient {
    @Value("${ml.bert.url}")
    private String bertUrl;

    private final RestTemplate rt = new RestTemplate();

    @Data
    public static class EmotionDTO {
        private String emotion;     // e.g. happy/angry/neutral/...
        private Double confidence;  // 0~1
    }

    /**
     * 调用情绪识别服务
     *
     * @param text 用户文本
     * @return 识别结果；失败则返回默认“中性”情绪
     */
    public EmotionDTO detect(String text) {
        try {
            Map<String, String> req = Map.of("text", text);
            EmotionDTO resp = rt.postForObject(bertUrl, req, EmotionDTO.class);
            if (resp == null) return fallback();
            if (resp.getEmotion() == null) resp.setEmotion("neutral");
            if (resp.getConfidence() == null) resp.setConfidence(0.0);
            return resp;
        } catch (Exception e) {
            return fallback();
        }
    }

    /**
     * 降级方案
     *
     * @return 默认“中性”情绪
     */
    private EmotionDTO fallback() {
        EmotionDTO dto = new EmotionDTO();
        dto.setEmotion("neutral");
        dto.setConfidence(0.0);
        return dto;
    }
}

