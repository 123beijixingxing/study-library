package com.studylib.modules.log.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.log.dto.LoginLogQueryDTO;
import com.studylib.modules.log.dto.OperationLogQueryDTO;
import com.studylib.modules.log.service.AdminLogService;
import com.studylib.modules.log.vo.LoginLogVO;
import com.studylib.modules.log.vo.OperationLogVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/logs")
@RequirePermission("logs:view")
public class AdminLogController {

  private final AdminLogService adminLogService;

  public AdminLogController(AdminLogService adminLogService) {
    this.adminLogService = adminLogService;
  }

  @GetMapping("/operations")
  public ApiResponse<PageResponse<OperationLogVO>> getOperationLogs(OperationLogQueryDTO query) {
    return ApiResponse.success(adminLogService.getOperationLogs(query));
  }

  @GetMapping("/logins")
  public ApiResponse<PageResponse<LoginLogVO>> getLoginLogs(LoginLogQueryDTO query) {
    return ApiResponse.success(adminLogService.getLoginLogs(query));
  }
}
