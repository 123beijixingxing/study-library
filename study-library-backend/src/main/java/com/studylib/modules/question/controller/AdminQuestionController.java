package com.studylib.modules.question.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.question.dto.QuestionImportRequestDTO;
import com.studylib.modules.question.dto.QuestionQueryDTO;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import com.studylib.modules.question.dto.QuestionStatusUpdateRequestDTO;
import com.studylib.modules.question.service.AdminQuestionService;
import com.studylib.modules.question.vo.QuestionImportResultVO;
import com.studylib.modules.question.vo.QuestionVO;
import com.studylib.modules.question.vo.QuestionVersionVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/questions")
@RequirePermission("course:manage")
public class AdminQuestionController {

  private final AdminQuestionService adminQuestionService;

  public AdminQuestionController(AdminQuestionService adminQuestionService) {
    this.adminQuestionService = adminQuestionService;
  }

  @GetMapping
  public ApiResponse<List<QuestionVO>> getQuestionList(QuestionQueryDTO query) {
    return ApiResponse.success(adminQuestionService.getQuestionList(query));
  }

  @GetMapping("/{questionId}")
  public ApiResponse<QuestionVO> getQuestionDetail(@PathVariable Long questionId) {
    return ApiResponse.success(adminQuestionService.getQuestionDetail(questionId));
  }

  @GetMapping("/{questionId}/versions")
  public ApiResponse<List<QuestionVersionVO>> getQuestionVersionHistory(@PathVariable Long questionId) {
    return ApiResponse.success(adminQuestionService.getQuestionVersionHistory(questionId));
  }

  @PostMapping
  public ApiResponse<QuestionVO> saveQuestion(@Valid @RequestBody QuestionSaveRequestDTO request) {
    return ApiResponse.success(adminQuestionService.saveQuestion(request));
  }

  @PatchMapping("/{questionId}/status")
  public ApiResponse<QuestionVO> updateQuestionStatus(@PathVariable Long questionId, @Valid @RequestBody QuestionStatusUpdateRequestDTO request) {
    return ApiResponse.success(adminQuestionService.updateQuestionStatus(questionId, request.getStatus()));
  }

  @PostMapping("/{questionId}/copy")
  public ApiResponse<QuestionVO> copyQuestion(@PathVariable Long questionId) {
    return ApiResponse.success(adminQuestionService.copyQuestion(questionId));
  }

  @PostMapping("/{questionId}/restore")
  public ApiResponse<QuestionVO> restoreQuestion(@PathVariable Long questionId) {
    return ApiResponse.success(adminQuestionService.restoreQuestion(questionId));
  }

  @PostMapping("/import")
  public ApiResponse<QuestionImportResultVO> importQuestions(@Valid @RequestBody QuestionImportRequestDTO request) {
    return ApiResponse.success(adminQuestionService.importQuestions(request));
  }
}
