package com.studylib.modules.course.service;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.course.dto.CourseEvaluationCreateRequestDTO;
import com.studylib.modules.course.vo.CourseDetailResponseVO;

public interface AppCourseService {
    CourseDetailResponseVO getCourseDetail(Long courseId);

    IdResponseVO createEvaluation(Long courseId, CourseEvaluationCreateRequestDTO request);
}
