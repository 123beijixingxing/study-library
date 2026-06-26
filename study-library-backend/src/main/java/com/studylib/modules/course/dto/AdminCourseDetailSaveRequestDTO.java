package com.studylib.modules.course.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminCourseDetailSaveRequestDTO {

  private Long courseId;

  @NotBlank(message = "teacherName is required")
  private String teacherName;

  @NotBlank(message = "summary is required")
  private String summary;

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
}
