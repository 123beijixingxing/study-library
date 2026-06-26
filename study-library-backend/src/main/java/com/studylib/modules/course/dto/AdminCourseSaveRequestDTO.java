package com.studylib.modules.course.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminCourseSaveRequestDTO {

  @NotBlank(message = "courseName is required")
  private String courseName;

  @NotBlank(message = "categoryName is required")
  private String categoryName;

  @Min(value = 0, message = "hotScore must be greater than or equal to 0")
  private Integer hotScore;

  @Min(value = 1, message = "chapterCount must be greater than 0")
  private Integer chapterCount;

  @NotBlank(message = "status is required")
  private String status;

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public Integer getHotScore() {
    return hotScore;
  }

  public void setHotScore(Integer hotScore) {
    this.hotScore = hotScore;
  }

  public Integer getChapterCount() {
    return chapterCount;
  }

  public void setChapterCount(Integer chapterCount) {
    this.chapterCount = chapterCount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
