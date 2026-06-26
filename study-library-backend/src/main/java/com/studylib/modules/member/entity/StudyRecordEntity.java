package com.studylib.modules.member.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class StudyRecordEntity extends BaseAuditEntity {

  private Long recordId;
  private String username;
  private String courseName;
  private Integer progressPercent;
  private LocalDateTime lastStudyTime;
  private String status;

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getProgressPercent() {
    return progressPercent;
  }

  public void setProgressPercent(Integer progressPercent) {
    this.progressPercent = progressPercent;
  }

  public LocalDateTime getLastStudyTime() {
    return lastStudyTime;
  }

  public void setLastStudyTime(LocalDateTime lastStudyTime) {
    this.lastStudyTime = lastStudyTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
