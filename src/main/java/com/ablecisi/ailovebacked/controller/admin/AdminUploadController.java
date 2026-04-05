package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.exception.BaseException;
import com.ablecisi.ailovebacked.properties.AliOssProperties;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminOssImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin/api/v1/uploads")
@RequiredArgsConstructor
public class AdminUploadController {

    private static final Set<String> ALLOWED_CT = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/jpg"
    );

    private final AdminOssImageUploadService ossImageUploadService;
    private final AliOssProperties aliOssProperties;

    /**
     * 上传图片到阿里云 OSS，返回公网 URL（前端保存文章封面、头像、帖子图等时写入数据库字段）。
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
        return Result.success(Map.of("url", url));
    }
}
