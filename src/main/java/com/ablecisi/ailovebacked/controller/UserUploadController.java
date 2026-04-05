package com.ablecisi.ailovebacked.controller;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.properties.AliOssProperties;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminOssImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * C 端用户上传（需 JWT），与管理端 OSS 逻辑一致，供 App 头像等使用。
 */
@RestController
@RequestMapping("/api/user/uploads")
@RequiredArgsConstructor
@Slf4j
public class UserUploadController {

    private static final Set<String> ALLOWED_CT = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/jpg");

    private final AdminOssImageUploadService ossImageUploadService;
    private final AliOssProperties aliOssProperties;

    /**
     * 上传图片
     *
     * @param file 图片文件
     * @return 图片 URL
     */
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Map<String, String>> uploadImage(@RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BaseException("请选择图片文件");
        }
        String ct = file.getContentType();
        String normalized = ct == null ? "" : ct.toLowerCase(Locale.ROOT);
        if (!ALLOWED_CT.contains(normalized)) {
            throw new BaseException("仅支持 jpeg/png/gif/webp 图片");
        }
        long max = aliOssProperties.getMaxImageBytes() > 0 ? aliOssProperties.getMaxImageBytes() : 0;
        String url = ossImageUploadService.uploadImage(file, ct, max);
        log.info("用户上传图片成功");
        return Result.success(Map.of("url", url));
    }
}
