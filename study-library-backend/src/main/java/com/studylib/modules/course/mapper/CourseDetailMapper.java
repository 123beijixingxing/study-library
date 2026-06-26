package com.studylib.modules.course.mapper;

import com.studylib.modules.course.entity.CourseDetailEntity;
import org.apache.ibatis.annotations.Param;

public interface CourseDetailMapper {

  CourseDetailEntity selectByCourseId(@Param("courseId") Long courseId);

  Integer insert(CourseDetailEntity entity);

  Integer update(CourseDetailEntity entity);
}
