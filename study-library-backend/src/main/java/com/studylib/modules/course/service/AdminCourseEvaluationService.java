package com.studylib.modules.course.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.modules.course.dto.CourseEvaluationQueryDTO;
import com.studylib.modules.course.dto.CourseEvaluationStatusUpdateRequestDTO;
import com.studylib.modules.course.vo.CourseEvaluationVO;

public interface AdminCourseEvaluationService {

  PageResponse<CourseEvaluationVO> getEvaluationList(CourseEvaluationQueryDTO query);

  CourseEvaluationVO updateEvaluationStatus(Long evaluationId, CourseEvaluationStatusUpdateRequestDTO request);
}
