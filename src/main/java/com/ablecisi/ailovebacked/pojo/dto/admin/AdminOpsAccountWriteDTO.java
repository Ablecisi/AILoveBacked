package com.ablecisi.ailovebacked.pojo.dto.admin;

import lombok.Data;

@Data
public class AdminOpsAccountWriteDTO {
    private String username;
    /**
     * 新建必填；更新时非空则改密
     */
    private String password;
}
