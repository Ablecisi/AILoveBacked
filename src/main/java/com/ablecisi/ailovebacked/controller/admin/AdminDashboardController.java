package com.ablecisi.ailovebacked.controller.admin;

import com.ablecisi.ailovebacked.pojo.vo.admin.AdminDashboardOverviewVO;
import com.ablecisi.ailovebacked.result.Result;
import com.ablecisi.ailovebacked.service.AdminDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/v1/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/overview")
    public Result<AdminDashboardOverviewVO> overview() {
        return Result.success(adminDashboardService.overview());
    }
}
