package com.studylib.modules.member.dto;

import com.studylib.common.dto.PageQueryDTO;

public class AdminMemberQueryDTO extends PageQueryDTO {

  private String keyword;
  private String status;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
