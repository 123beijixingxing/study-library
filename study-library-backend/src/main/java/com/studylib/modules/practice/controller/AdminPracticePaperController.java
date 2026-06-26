package com.studylib.modules.practice.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.practice.dto.PracticePaperDetailSaveRequestDTO;
import com.studylib.modules.practice.dto.PracticePaperQueryDTO;
import com.studylib.modules.practice.dto.PracticePaperSaveRequestDTO;
import com.studylib.modules.practice.service.AdminPracticePaperService;
import com.studylib.modules.practice.vo.PracticePaperDetailVO;
import com.studylib.modules.practice.vo.PracticePeerComparisonVO;
import com.studylib.modules.practice.vo.PracticePaperVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/admin/v1/practice-papers")
@RequirePermission("course:manage")
public class AdminPracticePaperController {

  private final AdminPracticePaperService adminPracticePaperService;

  public AdminPracticePaperController(AdminPracticePaperService adminPracticePaperService) {
    this.adminPracticePaperService = adminPracticePaperService;
  }

  @GetMapping
  public ApiResponse<PageResponse<PracticePaperVO>> getPaperList(PracticePaperQueryDTO query) {
    return ApiResponse.success(adminPracticePaperService.getPaperList(query));
  }

  @PostMapping
  public ApiResponse<PracticePaperVO> savePaper(@Valid @RequestBody PracticePaperSaveRequestDTO request) {
    return ApiResponse.success(adminPracticePaperService.savePaper(request));
  }

  @GetMapping("/{paperId}")
  public ApiResponse<PracticePaperDetailVO> getPaperDetail(@PathVariable Long paperId) {
    return ApiResponse.success(adminPracticePaperService.getPaperDetail(paperId));
  }

  @PutMapping("/{paperId}")
  public ApiResponse<PracticePaperDetailVO> savePaperDetail(@PathVariable Long paperId, @Valid @RequestBody PracticePaperDetailSaveRequestDTO request) {
    return ApiResponse.success(adminPracticePaperService.savePaperDetail(paperId, request));
  }

  @GetMapping("/{paperId}/peer-comparison")
  public ApiResponse<List<PracticePeerComparisonVO>> getPeerComparison(@PathVariable Long paperId) {
    return ApiResponse.success(adminPracticePaperService.getPeerComparison(paperId));
  }

  @DeleteMapping("/{paperId}")
  public ApiResponse<SuccessResponseVO> deletePaper(@PathVariable Long paperId) {
    return ApiResponse.success(adminPracticePaperService.deletePaper(paperId));
  }
}
