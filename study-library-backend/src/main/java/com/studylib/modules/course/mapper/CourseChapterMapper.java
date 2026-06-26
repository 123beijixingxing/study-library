package com.studylib.modules.course.mapper;

import com.studylib.modules.course.entity.CourseChapterEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseChapterMapper {

  CourseChapterEntity selectById(@Param("id") Long id);

  List<CourseChapterEntity> selectByCourseId(@Param("courseId") Long courseId);

  Integer insert(@Param("courseId") Long courseId, @Param("entity") CourseChapterEntity entity);

  Integer update(@Param("courseId") Long courseId, @Param("entity") CourseChapterEntity entity);
}
