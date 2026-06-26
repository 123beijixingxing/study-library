package com.studylib.modules.statistics.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.statistics.service.AdminStatisticsService;
import com.studylib.modules.statistics.vo.StatisticsOverviewVO;
import com.studylib.modules.statistics.vo.StatisticsTrendPointVO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/statistics")
@RequirePermission("dashboard:view")
public class AdminStatisticsController {

  private final AdminStatisticsService adminStatisticsService;

  public AdminStatisticsController(AdminStatisticsService adminStatisticsService) {
    this.adminStatisticsService = adminStatisticsService;
  }

  @GetMapping("/overview")
  public ApiResponse<StatisticsOverviewVO> getOverview() {
    return ApiResponse.success(adminStatisticsService.getOverview());
  }

  @GetMapping("/trends")
  public ApiResponse<List<StatisticsTrendPointVO>> getTrends() {
    return ApiResponse.success(adminStatisticsService.getTrends());
  }
}
