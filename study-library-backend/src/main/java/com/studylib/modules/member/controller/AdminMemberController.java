package com.studylib.modules.member.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.member.dto.AdminMemberQueryDTO;
import com.studylib.modules.member.dto.AdminMemberUpdateRequestDTO;
import com.studylib.modules.member.service.AdminMemberService;
import com.studylib.modules.member.vo.AdminMemberVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/members")
@RequirePermission("member:manage")
public class AdminMemberController {

  private final AdminMemberService adminMemberService;

  public AdminMemberController(AdminMemberService adminMemberService) {
    this.adminMemberService = adminMemberService;
  }

  @GetMapping
  public ApiResponse<PageResponse<AdminMemberVO>> getMemberList(AdminMemberQueryDTO query) {
    return ApiResponse.success(adminMemberService.getMemberList(query));
  }

  @PutMapping
  public ApiResponse<AdminMemberVO> updateMember(@Valid @RequestBody AdminMemberUpdateRequestDTO request) {
    return ApiResponse.success(adminMemberService.updateMember(request));
  }
}
