package com.studylib.modules.course.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.course.dto.AdminCourseChapterSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseDetailSaveRequestDTO;
import com.studylib.modules.course.dto.AdminCourseQueryDTO;
import com.studylib.modules.course.dto.AdminCourseSaveRequestDTO;
import com.studylib.modules.course.service.AdminCourseService;
import com.studylib.modules.course.vo.AdminCourseDetailResponseVO;
import com.studylib.modules.course.vo.AdminCourseDetailVO;
import com.studylib.modules.course.vo.AdminCourseListItemVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/courses")
@RequirePermission("course:manage")
public class AdminCourseController {

  private final AdminCourseService adminCourseService;

  public AdminCourseController(AdminCourseService adminCourseService) {
    this.adminCourseService = adminCourseService;
  }

  @GetMapping
  public ApiResponse<PageResponse<AdminCourseListItemVO>> getCourseList(@Valid AdminCourseQueryDTO query) {
    return ApiResponse.success(adminCourseService.getCourseList(query));
  }

  @PostMapping
  public ApiResponse<AdminCourseListItemVO> createCourse(@Valid @RequestBody AdminCourseSaveRequestDTO request) {
    return ApiResponse.success(adminCourseService.createCourse(request));
  }

  @PutMapping("/{courseId}")
  public ApiResponse<AdminCourseListItemVO> updateCourse(@PathVariable Long courseId, @Valid @RequestBody AdminCourseSaveRequestDTO request) {
    return ApiResponse.success(adminCourseService.updateCourse(courseId, request));
  }

  @DeleteMapping("/{courseId}")
  public ApiResponse<SuccessResponseVO> deleteCourse(@PathVariable Long courseId) {
    return ApiResponse.success(adminCourseService.deleteCourse(courseId));
  }

  @GetMapping("/{courseId}")
  public ApiResponse<AdminCourseDetailResponseVO> getCourseDetail(@PathVariable Long courseId) {
    return ApiResponse.success(adminCourseService.getCourseDetail(courseId));
  }

  @PutMapping("/{courseId}/detail")
  public ApiResponse<AdminCourseDetailVO> saveCourseDetail(@PathVariable Long courseId,
      @Valid @RequestBody AdminCourseDetailSaveRequestDTO request) {
    return ApiResponse.success(adminCourseService.saveCourseDetail(courseId, request));
  }

  @PostMapping("/{courseId}/chapters")
  public ApiResponse<AdminCourseDetailVO> saveCourseChapter(@PathVariable Long courseId,
      @Valid @RequestBody AdminCourseChapterSaveRequestDTO request) {
    return ApiResponse.success(adminCourseService.saveCourseChapter(courseId, request));
  }
}
