package com.studylib.modules.course.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailEntity extends BaseAuditEntity {

  private Long courseId;
  private String teacherName;
  private String summary;
  private List<CourseChapterEntity> chapterList = new ArrayList<>();

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<CourseChapterEntity> getChapterList() {
    return chapterList;
  }

  public void setChapterList(List<CourseChapterEntity> chapterList) {
    this.chapterList = chapterList;
  }
}
