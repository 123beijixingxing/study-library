package com.studylib.modules.service.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class ServiceTicketEntity extends BaseAuditEntity {

  private Long id;
  private String userName;
  private String latestMessage;
  private String priorityLevel;
  private String status;
  private LocalDateTime updateTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getLatestMessage() {
    return latestMessage;
  }

  public void setLatestMessage(String latestMessage) {
    this.latestMessage = latestMessage;
  }

  public String getPriorityLevel() {
    return priorityLevel;
  }

  public void setPriorityLevel(String priorityLevel) {
    this.priorityLevel = priorityLevel;
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
