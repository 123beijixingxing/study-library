package com.studylib.modules.course.assembler;

import com.studylib.modules.course.entity.CourseChapterEntity;
import com.studylib.modules.course.entity.CourseDetailEntity;
import com.studylib.modules.course.entity.CourseEntity;
import com.studylib.modules.course.vo.AdminCourseChapterVO;
import com.studylib.modules.course.vo.AdminCourseDetailResponseVO;
import com.studylib.modules.course.vo.AdminCourseDetailVO;
import com.studylib.modules.course.vo.AdminCourseListItemVO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AdminCourseAssembler {

  public AdminCourseListItemVO toListItem(CourseEntity entity) {
    return new AdminCourseListItemVO(
        entity.getId(),
        entity.getCourseName(),
        entity.getCategoryName(),
        entity.getHotScore(),
        entity.getChapterCount(),
        entity.getStatus(),
        entity.getUpdateTime()
    );
  }

  public AdminCourseChapterVO toChapter(CourseChapterEntity entity) {
    return new AdminCourseChapterVO(entity.getId(), entity.getTitle(), entity.getDurationMinutes(), entity.getStatus());
  }

  public AdminCourseDetailVO toDetail(CourseDetailEntity entity) {
    List<AdminCourseChapterVO> chapterList = entity.getChapterList().stream().map(this::toChapter).toList();
    return new AdminCourseDetailVO(entity.getCourseId(), entity.getTeacherName(), entity.getSummary(), chapterList);
  }

  public AdminCourseDetailResponseVO toDetailResponse(CourseEntity course, CourseDetailEntity detail) {
    return new AdminCourseDetailResponseVO(toListItem(course), toDetail(detail));
  }
}
