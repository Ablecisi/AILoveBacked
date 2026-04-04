package com.ablecisi.ailovebacked.config;

import com.ablecisi.ailovebacked.mapper.AdminMapper;
import com.ablecisi.ailovebacked.pojo.entity.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(20)
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (adminMapper.count() > 0) {
            return;
        }
        AdminUser u = new AdminUser();
        u.setUsername("admin");
        u.setPassword(passwordEncoder.encode("admin123"));
        adminMapper.insert(u);
        log.warn("已初始化默认管理员账号 admin / admin123，生产环境请立即修改密码");
    }
}
