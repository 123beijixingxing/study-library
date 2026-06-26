package com.studylib.modules.home.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.home.dto.HomeSectionQueryDTO;
import com.studylib.modules.home.dto.HomeSectionUpdateRequestDTO;
import com.studylib.modules.home.service.AdminHomeService;
import com.studylib.modules.home.vo.HomeSectionVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/home/courses")
@RequirePermission("course:manage")
public class AdminHomeController {

  private final AdminHomeService adminHomeService;

  public AdminHomeController(AdminHomeService adminHomeService) {
    this.adminHomeService = adminHomeService;
  }

  @GetMapping
  public ApiResponse<PageResponse<HomeSectionVO>> getSectionList(HomeSectionQueryDTO query) {
    return ApiResponse.success(adminHomeService.getSectionList(query));
  }

  @PutMapping
  public ApiResponse<HomeSectionVO> updateSection(@Valid @RequestBody HomeSectionUpdateRequestDTO request) {
    return ApiResponse.success(adminHomeService.updateSection(request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<SuccessResponseVO> deleteSection(@PathVariable Long id) {
    return ApiResponse.success(adminHomeService.deleteSection(id));
  }
}
