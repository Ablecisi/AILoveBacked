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

}
