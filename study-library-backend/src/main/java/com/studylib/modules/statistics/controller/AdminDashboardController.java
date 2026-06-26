package com.studylib.modules.statistics.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.statistics.service.AdminDashboardService;
import com.studylib.modules.statistics.vo.DashboardSummaryVO;
import com.studylib.modules.statistics.vo.DashboardTrendPointVO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/dashboard")
@RequirePermission("dashboard:view")
public class AdminDashboardController {

  private final AdminDashboardService adminDashboardService;

  public AdminDashboardController(AdminDashboardService adminDashboardService) {
    this.adminDashboardService = adminDashboardService;
  }

  @GetMapping("/summary")
  public ApiResponse<DashboardSummaryVO> getSummary() {
    return ApiResponse.success(adminDashboardService.getSummary());
  }

  @GetMapping("/user-trend")
  public ApiResponse<List<DashboardTrendPointVO>> getUserTrend() {
    return ApiResponse.success(adminDashboardService.getUserTrend());
  }
}
