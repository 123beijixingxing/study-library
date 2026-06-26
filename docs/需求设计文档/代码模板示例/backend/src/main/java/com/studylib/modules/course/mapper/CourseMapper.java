package com.studylib.modules.course.mapper;

import java.util.List;

public interface CourseMapper {
    Object selectById(Long courseId);

    List<Object> selectPublishedChaptersByCourseId(Long courseId);

    boolean existsVisibleEvaluation(Long courseId, Long userId);

    int insertEvaluation(Object evaluationEntity);
}
