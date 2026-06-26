package com.studylib.modules.course.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class CourseEvaluationEntity extends BaseAuditEntity {

  private Long evaluationId;
  private String courseName;
  private String username;
  private Integer score;
  private String content;
  private String status;
  private LocalDateTime createTime;

  public Long getEvaluationId() {
    return evaluationId;
  }

  public void setEvaluationId(Long evaluationId) {
    this.evaluationId = evaluationId;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }
}
