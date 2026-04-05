package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.utils.HttpClientUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * EmotionClient：通过 {@link HttpClientUtil} 调用情绪识别 HTTP 服务。
 */
@Service
public class EmotionClient {
    @Value("${ml.bert.url}")
    private String bertUrl;

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
            HttpClientUtil.HttpResult r = HttpClientUtil.postJson(bertUrl, req, Map.of());
            if (!r.is2xx() || r.body() == null || r.body().isBlank()) {
                return fallback();
            }
            EmotionDTO resp = r.parseBody(EmotionDTO.class);
            if (resp == null) {
                return fallback();
            }
            if (resp.getEmotion() == null) {
                resp.setEmotion("neutral");
            }
            if (resp.getConfidence() == null) {
                resp.setConfidence(0.0);
            }
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
