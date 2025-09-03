package com.ablecisi.ailovebacked.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * AILoveBacked <br>
 * com.ablecisi.ailovebacked.result <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/8/29
 * 星期五
 * 00:00
 **/
@Data
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private List<T> records;
}
