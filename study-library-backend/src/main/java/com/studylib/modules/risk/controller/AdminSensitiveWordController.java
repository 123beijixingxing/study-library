package com.studylib.modules.risk.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.risk.dto.SensitiveWordQueryDTO;
import com.studylib.modules.risk.dto.SensitiveWordSaveRequestDTO;
import com.studylib.modules.risk.service.AdminSensitiveWordService;
import com.studylib.modules.risk.vo.SensitiveWordVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/sensitive-words")
@RequirePermission("garden:manage")
public class AdminSensitiveWordController {

  private final AdminSensitiveWordService adminSensitiveWordService;

  public AdminSensitiveWordController(AdminSensitiveWordService adminSensitiveWordService) {
    this.adminSensitiveWordService = adminSensitiveWordService;
  }

  @GetMapping
  public ApiResponse<PageResponse<SensitiveWordVO>> getWordList(SensitiveWordQueryDTO query) {
    return ApiResponse.success(adminSensitiveWordService.getWordList(query));
  }

  @PostMapping
  public ApiResponse<SensitiveWordVO> saveWord(@Valid @RequestBody SensitiveWordSaveRequestDTO request) {
    return ApiResponse.success(adminSensitiveWordService.saveWord(request));
  }

  @DeleteMapping("/{wordId}")
  public ApiResponse<SuccessResponseVO> deleteWord(@PathVariable Long wordId) {
    return ApiResponse.success(adminSensitiveWordService.deleteWord(wordId));
  }
}
