package com.studylib.modules.course.mapper;

import com.studylib.modules.course.entity.CourseEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CourseMapper {

  CourseEntity selectById(@Param("id") Long id);

  Long countPage(@Param("keyword") String keyword, @Param("categoryName") String categoryName, @Param("status") String status);

  List<CourseEntity> selectPage(@Param("keyword") String keyword, @Param("categoryName") String categoryName, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(CourseEntity entity);

  Integer update(CourseEntity entity);

  Integer updateChapterCount(@Param("id") Long id, @Param("chapterCount") Integer chapterCount, @Param("updateTime") LocalDateTime updateTime,
      @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
