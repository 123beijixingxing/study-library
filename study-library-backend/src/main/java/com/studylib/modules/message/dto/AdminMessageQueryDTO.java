package com.studylib.modules.message.dto;

import com.studylib.common.dto.PageQueryDTO;

public class AdminMessageQueryDTO extends PageQueryDTO {

  private String keyword;
  private String sendStatus;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getSendStatus() {
    return sendStatus;
  }

  public void setSendStatus(String sendStatus) {
    this.sendStatus = sendStatus;
  }
}
