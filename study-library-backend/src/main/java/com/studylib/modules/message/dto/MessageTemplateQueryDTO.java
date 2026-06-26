package com.studylib.modules.message.dto;

import com.studylib.common.dto.PageQueryDTO;

public class MessageTemplateQueryDTO extends PageQueryDTO {

  private String messageType;
  private String status;

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
