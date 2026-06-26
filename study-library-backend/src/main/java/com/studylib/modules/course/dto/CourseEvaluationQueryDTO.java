package com.studylib.modules.course.dto;

import com.studylib.common.dto.PageQueryDTO;

public class CourseEvaluationQueryDTO extends PageQueryDTO {

  private String keyword;
  private String status;
  private String courseName;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }
}
