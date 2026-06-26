package com.studylib.modules.garden.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.garden.dto.GardenAuditRequestDTO;
import com.studylib.modules.garden.dto.GardenContentQueryDTO;
import com.studylib.modules.garden.service.AdminGardenService;
import com.studylib.modules.garden.vo.GardenContentVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/contents")
@RequirePermission("garden:manage")
public class AdminGardenController {

  private final AdminGardenService adminGardenService;

  public AdminGardenController(AdminGardenService adminGardenService) {
    this.adminGardenService = adminGardenService;
  }

  @GetMapping
  public ApiResponse<PageResponse<GardenContentVO>> getContentList(GardenContentQueryDTO query) {
    return ApiResponse.success(adminGardenService.getContentList(query));
  }

  @PostMapping("/{contentId}/audit")
  public ApiResponse<GardenContentVO> auditContent(@PathVariable Long contentId, @Valid @RequestBody GardenAuditRequestDTO request) {
    return ApiResponse.success(adminGardenService.auditContent(contentId, request));
  }

  @DeleteMapping("/{contentId}")
  public ApiResponse<SuccessResponseVO> deleteContent(@PathVariable Long contentId) {
    return ApiResponse.success(adminGardenService.deleteContent(contentId));
  }
}
