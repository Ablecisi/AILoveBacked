package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminOpsAccountVO {
    private Long id;
    private String username;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
