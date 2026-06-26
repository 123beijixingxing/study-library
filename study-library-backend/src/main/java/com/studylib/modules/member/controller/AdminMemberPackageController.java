package com.studylib.modules.member.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.member.dto.MemberPackageQueryDTO;
import com.studylib.modules.member.dto.MemberPackageSaveRequestDTO;
import com.studylib.modules.member.service.AdminMemberPackageService;
import com.studylib.modules.member.vo.MemberPackageVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/member-packages")
@RequirePermission("member:manage")
public class AdminMemberPackageController {

  private final AdminMemberPackageService adminMemberPackageService;

  public AdminMemberPackageController(AdminMemberPackageService adminMemberPackageService) {
    this.adminMemberPackageService = adminMemberPackageService;
  }

  @GetMapping
  public ApiResponse<PageResponse<MemberPackageVO>> getPackageList(MemberPackageQueryDTO query) {
    return ApiResponse.success(adminMemberPackageService.getPackageList(query));
  }

  @PostMapping
  public ApiResponse<MemberPackageVO> savePackage(@Valid @RequestBody MemberPackageSaveRequestDTO request) {
    return ApiResponse.success(adminMemberPackageService.savePackage(request));
  }

  @DeleteMapping("/{packageId}")
  public ApiResponse<SuccessResponseVO> deletePackage(@PathVariable Long packageId) {
    return ApiResponse.success(adminMemberPackageService.deletePackage(packageId));
  }
}
