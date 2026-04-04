package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.dto.AdminLoginDTO;
import com.ablecisi.ailovebacked.pojo.vo.AdminLoginVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody @Valid AdminLoginDTO dto) {
        return Result.success("登录成功", adminAuthService.login(dto));
    }
}
