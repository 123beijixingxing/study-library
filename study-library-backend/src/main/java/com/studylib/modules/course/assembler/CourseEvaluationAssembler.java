package com.studylib.modules.course.assembler;

import com.studylib.modules.course.entity.CourseEvaluationEntity;
import com.studylib.modules.course.vo.CourseEvaluationVO;
import org.springframework.stereotype.Component;

@Component
public class CourseEvaluationAssembler {

  public CourseEvaluationVO toVO(CourseEvaluationEntity entity) {
    return new CourseEvaluationVO(
        entity.getEvaluationId(),
        entity.getCourseName(),
        entity.getUsername(),
        entity.getScore(),
        entity.getContent(),
        entity.getStatus(),
        entity.getCreateTime()
    );
  }
}
