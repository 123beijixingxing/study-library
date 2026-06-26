package com.studylib.modules.risk.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;
import com.studylib.modules.risk.dto.ReportQueryDTO;
import com.studylib.modules.risk.service.AdminReportService;
import com.studylib.modules.risk.vo.ReportVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/reports")
@RequirePermission("garden:manage")
public class AdminReportController {

  private final AdminReportService adminReportService;

  public AdminReportController(AdminReportService adminReportService) {
    this.adminReportService = adminReportService;
  }

  @GetMapping
  public ApiResponse<PageResponse<ReportVO>> getReportList(ReportQueryDTO query) {
    return ApiResponse.success(adminReportService.getReportList(query));
  }

  @PostMapping("/{reportId}/handle")
  public ApiResponse<ReportVO> handleReport(@PathVariable Long reportId, @Valid @RequestBody ReportHandleRequestDTO request) {
    return ApiResponse.success(adminReportService.handleReport(reportId, request));
  }
}
