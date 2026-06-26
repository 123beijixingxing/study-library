package com.studylib.modules.practice.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.practice.service.AdminPracticePaperService;
import com.studylib.modules.practice.vo.PracticeAnalysisVO;
import com.studylib.modules.practice.vo.WrongQuestionVO;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequirePermission("course:manage")
public class AdminPracticeAnalysisController {

  private final AdminPracticePaperService adminPracticePaperService;

  public AdminPracticeAnalysisController(AdminPracticePaperService adminPracticePaperService) {
    this.adminPracticePaperService = adminPracticePaperService;
  }

  @GetMapping("/api/admin/v1/practice-records/analysis")
  public ApiResponse<PracticeAnalysisVO> getPracticeAnalysis(@RequestParam Long paperId) {
    return ApiResponse.success(adminPracticePaperService.getPracticeAnalysis(paperId));
  }

  @GetMapping("/api/admin/v1/wrong-questions/analysis")
  public ApiResponse<List<WrongQuestionVO>> getWrongQuestionAnalysis(@RequestParam Long paperId) {
    return ApiResponse.success(adminPracticePaperService.getWrongQuestionAnalysis(paperId));
  }
}
