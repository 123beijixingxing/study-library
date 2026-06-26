package com.studylib.modules.system.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.system.dto.SystemFeatureUpdateRequestDTO;
import com.studylib.modules.system.dto.SystemInfoUpdateRequestDTO;
import com.studylib.modules.system.service.AdminSystemService;
import com.studylib.modules.system.vo.SystemFeatureVO;
import com.studylib.modules.system.vo.SystemInfoVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/system")
@RequirePermission("system:manage")
public class AdminSystemController {

  private final AdminSystemService adminSystemService;

  public AdminSystemController(AdminSystemService adminSystemService) {
    this.adminSystemService = adminSystemService;
  }

  @GetMapping("/info")
  public ApiResponse<SystemInfoVO> getSystemInfo() {
    return ApiResponse.success(adminSystemService.getSystemInfo());
  }

  @PutMapping("/info")
  public ApiResponse<SystemInfoVO> updateSystemInfo(@Valid @RequestBody SystemInfoUpdateRequestDTO request) {
    return ApiResponse.success(adminSystemService.updateSystemInfo(request));
  }

  @GetMapping("/features")
  public ApiResponse<List<SystemFeatureVO>> getFeatureList() {
    return ApiResponse.success(adminSystemService.getFeatureList());
  }

  @PutMapping("/features")
  public ApiResponse<SystemFeatureVO> updateFeature(@Valid @RequestBody SystemFeatureUpdateRequestDTO request) {
    return ApiResponse.success(adminSystemService.updateFeature(request));
  }
}
