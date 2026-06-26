package com.studylib.modules.course.dto;

import com.studylib.common.dto.PageQueryDTO;

public class AdminCourseQueryDTO extends PageQueryDTO {

  private String categoryName;
  private String status;

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
