package com.studylib.modules.course.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class CourseChapterEntity extends BaseAuditEntity {

  private Long id;
  private String title;
  private Integer durationMinutes;
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
