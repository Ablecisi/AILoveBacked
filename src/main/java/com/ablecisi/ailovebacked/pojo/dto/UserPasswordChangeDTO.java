package com.ablecisi.ailovebacked.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordChangeDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    @Size(min = 6, max = 64, message = "新密码长度为 6～64 位")
    private String newPassword;
}
