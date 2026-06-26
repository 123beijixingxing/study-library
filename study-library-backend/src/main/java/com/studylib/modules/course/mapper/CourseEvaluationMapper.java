package com.studylib.modules.course.mapper;

import com.studylib.modules.course.entity.CourseEvaluationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseEvaluationMapper {

  CourseEvaluationEntity selectById(@Param("evaluationId") Long evaluationId);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status, @Param("courseName") String courseName);

  List<CourseEvaluationEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status, @Param("courseName") String courseName,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer updateStatus(@Param("evaluationId") Long evaluationId, @Param("status") String status, @Param("deleted") Boolean deleted,
      @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
