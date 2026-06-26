package com.studylib.modules.course.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class CourseEntity extends BaseAuditEntity {

  private Long id;
  private String courseName;
  private String categoryName;
  private Integer hotScore;
  private Integer chapterCount;
  private String status;
  private LocalDateTime updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
