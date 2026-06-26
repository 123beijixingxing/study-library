package com.studylib.modules.course.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.course.dto.CourseEvaluationQueryDTO;
import com.studylib.modules.course.dto.CourseEvaluationStatusUpdateRequestDTO;
import com.studylib.modules.course.service.AdminCourseEvaluationService;
import com.studylib.modules.course.vo.CourseEvaluationVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/course-evaluations")
@RequirePermission("course:manage")
public class AdminCourseEvaluationController {

  private final AdminCourseEvaluationService adminCourseEvaluationService;

  public AdminCourseEvaluationController(AdminCourseEvaluationService adminCourseEvaluationService) {
    this.adminCourseEvaluationService = adminCourseEvaluationService;
  }

  @GetMapping
  public ApiResponse<PageResponse<CourseEvaluationVO>> getEvaluationList(CourseEvaluationQueryDTO query) {
    return ApiResponse.success(adminCourseEvaluationService.getEvaluationList(query));
  }

  @PatchMapping("/{evaluationId}/status")
  public ApiResponse<CourseEvaluationVO> updateEvaluationStatus(@PathVariable Long evaluationId,
      @Valid @RequestBody CourseEvaluationStatusUpdateRequestDTO request) {
    return ApiResponse.success(adminCourseEvaluationService.updateEvaluationStatus(evaluationId, request));
  }
}
