package com.ablecisi.ailovebacked.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AILoveBacked
 * com.ablecisi.ailovebacked.entity.dto
 * UserDTO <br>
 *
 * @author Ablecisi
 * @version 1.0
 * 2025/4/25
 * 星期五
 * 22:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String username;
    private String password;
}
