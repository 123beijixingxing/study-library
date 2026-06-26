package com.studylib.modules.statistics.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDate;

public class StatisticsDailyMetricEntity extends BaseAuditEntity {

  private LocalDate metricDate;
  private Integer newUsers;
  private Integer activeUsers;
  private Integer courseViews;
  private Integer contentInteractions;

  public LocalDate getMetricDate() {
    return metricDate;
  }

  public void setMetricDate(LocalDate metricDate) {
    this.metricDate = metricDate;
  }

  public Integer getNewUsers() {
    return newUsers;
  }

  public void setNewUsers(Integer newUsers) {
    this.newUsers = newUsers;
  }

  public Integer getActiveUsers() {
    return activeUsers;
  }

  public void setActiveUsers(Integer activeUsers) {
    this.activeUsers = activeUsers;
  }

  public Integer getCourseViews() {
    return courseViews;
  }

  public void setCourseViews(Integer courseViews) {
    this.courseViews = courseViews;
  }

  public Integer getContentInteractions() {
    return contentInteractions;
  }

  public void setContentInteractions(Integer contentInteractions) {
    this.contentInteractions = contentInteractions;
  }
}
