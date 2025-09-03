package com.ablecisi.ailovebacked.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.pojo.vo <br>
 *
 * @author Ablecisi
 * @version 0.0.1
 * 2025/9/1
 * 星期一
 * 08:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootTreeVO implements Serializable {
    private CommentVO root;            // 顶层评论
    private List<CommentVO> descendants; // 该顶层下所有子孙（按 path 有序）
}
