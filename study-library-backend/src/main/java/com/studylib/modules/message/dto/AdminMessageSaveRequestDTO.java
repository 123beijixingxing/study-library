package com.studylib.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminMessageSaveRequestDTO {

  private Long id;

  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "messageType is required")
  private String messageType;

  @NotBlank(message = "receiverType is required")
  private String receiverType;

  @NotBlank(message = "sendStatus is required")
  private String sendStatus;

  private String sendTime;

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

  public String getSendTime() {
    return sendTime;
  }

  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }
}
