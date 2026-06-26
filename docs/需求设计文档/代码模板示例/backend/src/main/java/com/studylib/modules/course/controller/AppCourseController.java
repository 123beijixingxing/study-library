package com.studylib.modules.course.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.course.dto.CourseEvaluationCreateRequestDTO;
import com.studylib.modules.course.service.AppCourseService;
import com.studylib.modules.course.vo.CourseDetailResponseVO;

public class AppCourseController {
    private final AppCourseService appCourseService;

    public AppCourseController(AppCourseService appCourseService) {
        this.appCourseService = appCourseService;
    }

    public ApiResponse<CourseDetailResponseVO> detail(Long courseId) {
        return ApiResponse.success(appCourseService.getCourseDetail(courseId));
    }

    public ApiResponse<IdResponseVO> createEvaluation(Long courseId, CourseEvaluationCreateRequestDTO request) {
        return ApiResponse.success(appCourseService.createEvaluation(courseId, request));
    }
}
