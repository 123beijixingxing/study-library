package com.studylib.modules.member.dto;

import com.studylib.common.dto.PageQueryDTO;

public class MemberPackageQueryDTO extends PageQueryDTO {

  private String status;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
