package com.studylib.modules.message.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class AdminMessageEntity extends BaseAuditEntity {

  private Long id;
  private String title;
  private String messageType;
  private String receiverType;
  private String sendStatus;
  private LocalDateTime sendTime;

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

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getReceiverType() {
    return receiverType;
  }

  public void setReceiverType(String receiverType) {
    this.receiverType = receiverType;
  }

  public String getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(String sendStatus) {
    this.sendStatus = sendStatus;
  }

  public LocalDateTime getSendTime() {
    return sendTime;
  }

  public void setSendTime(LocalDateTime sendTime) {
    this.sendTime = sendTime;
  }
}
