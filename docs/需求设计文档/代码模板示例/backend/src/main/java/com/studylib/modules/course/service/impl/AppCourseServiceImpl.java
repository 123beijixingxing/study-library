package com.studylib.modules.course.service.impl;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.course.dto.CourseEvaluationCreateRequestDTO;
import com.studylib.modules.course.mapper.CourseMapper;
import com.studylib.modules.course.service.AppCourseService;
import com.studylib.modules.course.vo.CourseDetailResponseVO;

public class AppCourseServiceImpl implements AppCourseService {
    private final CourseMapper courseMapper;

    public AppCourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseDetailResponseVO getCourseDetail(Long courseId) {
        throw new UnsupportedOperationException("Implement course detail query and VO assembly");
    }

    @Override
    public IdResponseVO createEvaluation(Long courseId, CourseEvaluationCreateRequestDTO request) {
        throw new UnsupportedOperationException("Implement evaluation creation with duplicate-check");
    }
}
