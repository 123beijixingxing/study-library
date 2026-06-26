package com.studylib.modules.member.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.member.dto.GrowthRuleSaveRequestDTO;
import com.studylib.modules.member.service.AdminGrowthRuleService;
import com.studylib.modules.member.vo.GrowthRuleVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/growth-rules")
@RequirePermission("member:manage")
public class AdminGrowthRuleController {

  private final AdminGrowthRuleService adminGrowthRuleService;

  public AdminGrowthRuleController(AdminGrowthRuleService adminGrowthRuleService) {
    this.adminGrowthRuleService = adminGrowthRuleService;
  }

  @GetMapping
  public ApiResponse<List<GrowthRuleVO>> getRuleList() {
    return ApiResponse.success(adminGrowthRuleService.getRuleList());
  }

  @PutMapping
  public ApiResponse<GrowthRuleVO> saveRule(@Valid @RequestBody GrowthRuleSaveRequestDTO request) {
    return ApiResponse.success(adminGrowthRuleService.saveRule(request));
  }
}
