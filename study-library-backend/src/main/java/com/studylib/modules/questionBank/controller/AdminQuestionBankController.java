package com.studylib.modules.questionBank.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.questionBank.dto.QuestionBankQueryDTO;
import com.studylib.modules.questionBank.dto.QuestionBankSaveRequestDTO;
import com.studylib.modules.questionBank.service.AdminQuestionBankService;
import com.studylib.modules.questionBank.vo.QuestionBankVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/question-banks")
@RequirePermission("course:manage")
public class AdminQuestionBankController {

  private final AdminQuestionBankService adminQuestionBankService;

  public AdminQuestionBankController(AdminQuestionBankService adminQuestionBankService) {
    this.adminQuestionBankService = adminQuestionBankService;
  }

  @GetMapping
  public ApiResponse<PageResponse<QuestionBankVO>> getQuestionBankList(QuestionBankQueryDTO query) {
    return ApiResponse.success(adminQuestionBankService.getQuestionBankList(query));
  }

  @PostMapping
  public ApiResponse<QuestionBankVO> saveQuestionBank(@Valid @RequestBody QuestionBankSaveRequestDTO request) {
    return ApiResponse.success(adminQuestionBankService.saveQuestionBank(request));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<SuccessResponseVO> deleteQuestionBank(@PathVariable Long id) {
    return ApiResponse.success(adminQuestionBankService.deleteQuestionBank(id));
  }
}
