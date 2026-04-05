package com.ablecisi.ailovebacked.service;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.properties.AliOssProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

/**
 * 管理端图片上传到阿里云 OSS，返回可写入库表的公网访问 URL。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminOssImageUploadService {

    private final OSS ossClient;
    private final AliOssProperties ossProperties;

    /**
     * @param file        上传文件
     * @param contentType MIME，用于 OSS Content-Type
     * @param maxBytes    上限字节，&lt;=0 时用配置 {@link AliOssProperties#getMaxImageBytes()}
     */
    public String uploadImage(MultipartFile file, String contentType, long maxBytes) {
        if (file == null || file.isEmpty()) {
            throw new BaseException("请选择图片文件");
        }
        long configured = ossProperties.getMaxImageBytes();
        long limit = maxBytes > 0 ? maxBytes : (configured > 0 ? configured : 5L * 1024 * 1024);
        if (file.getSize() > limit) {
            throw new BaseException("图片过大");
        }
        String bucket = ossProperties.getBucketName();
        if (bucket == null || bucket.isBlank()) {
            throw new BaseException("未配置 OSS bucketName");
        }
        String endpoint = ossProperties.getEndpoint();
        if (endpoint == null || endpoint.isBlank()) {
            throw new BaseException("未配置 OSS endpoint");
        }

        String folder = normalizeFolder(ossProperties.getFolderName());
        LocalDate d = LocalDate.now();
        String sub = d.getYear() + "/" + String.format("%02d", d.getMonthValue());
        String ext = extensionFromContentType(contentType);
        String name = UUID.randomUUID().toString().replace("-", "") + ext;
        String objectKey = folder.isEmpty() ? sub + "/" + name : folder + "/" + sub + "/" + name;

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getSize());
        if (contentType != null && !contentType.isBlank()) {
            meta.setContentType(contentType);
        }

        try (InputStream in = file.getInputStream()) {
            ossClient.putObject(bucket, objectKey, in, meta);
        } catch (Exception e) {
            log.warn("OSS putObject failed, key={}", objectKey, e);
            throw new BaseException("上传至 OSS 失败：" + e.getMessage());
        }

        return buildPublicUrl(objectKey);
    }

    private static String normalizeFolder(String folderName) {
        if (folderName == null) {
            return "";
        }
        String s = folderName.trim().replace('\\', '/');
        while (s.startsWith("/")) {
            s = s.substring(1);
        }
        while (s.endsWith("/")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 若配置了 {@link AliOssProperties#getPublicBaseUrl()}（如 CDN/自定义域名），则返回该前缀 + objectKey；
     * 否则使用虚拟主机样式：https://{bucket}.{endpoint}/{objectKey}
     */
    private String buildPublicUrl(String objectKey) {
        String custom = ossProperties.getPublicBaseUrl();
        if (custom != null && !custom.isBlank()) {
            String base = custom.replaceAll("/+$", "");
            String key = objectKey.startsWith("/") ? objectKey.substring(1) : objectKey;
            return base + "/" + key;
        }
        String ep = ossProperties.getEndpoint().replaceFirst("(?i)^https?://", "").replaceAll("/+$", "");
        String bkt = ossProperties.getBucketName();
        return "https://" + bkt + "." + ep + "/" + objectKey;
    }

    private static String extensionFromContentType(String ct) {
        if (ct == null) {
            return ".jpg";
        }
        String c = ct.toLowerCase(Locale.ROOT);
        if (c.contains("png")) {
            return ".png";
        }
        if (c.contains("gif")) {
            return ".gif";
        }
        if (c.contains("webp")) {
            return ".webp";
        }
        return ".jpg";
    }
}
