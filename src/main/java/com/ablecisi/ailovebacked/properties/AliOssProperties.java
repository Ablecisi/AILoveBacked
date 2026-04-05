package com.ablecisi.ailovebacked.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置
 */
@Component
@ConfigurationProperties(prefix = "ailove.aliyun.oss")
@Data
public class AliOssProperties {

    private String endpoint; // oss地域节点
    private String accessKeyId; // oss访问key
    private String accessKeySecret; // oss访问密钥 // 真实开发中可以加密存储
    private String bucketName; // oss存储空间
    private String folderName; // oss文件夹名称

    /**
     * 可选。绑定 CDN 或自定义域名时填写（无尾斜杠），返回给前端的 url = publicBaseUrl + "/" + objectKey
     */
    private String publicBaseUrl;

    /**
     * 管理端单图最大字节，默认 5MiB；0 表示使用服务内建默认
     */
    private long maxImageBytes = 5L * 1024 * 1024;
}
