package com.ablecisi.ailovebacked.pojo.vo.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DauCountRow {
    private LocalDate activeDate;
    private long cnt;
}
