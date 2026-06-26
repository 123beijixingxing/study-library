package com.studylib.modules.risk.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;
import com.studylib.modules.risk.service.AdminReportService;

public class AdminReportController {
    private final AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    public ApiResponse<SuccessResponseVO> handle(Long reportId, ReportHandleRequestDTO request) {
        return ApiResponse.success(adminReportService.handleReport(reportId, request));
    }
}
