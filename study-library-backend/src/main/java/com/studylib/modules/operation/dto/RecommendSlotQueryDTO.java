package com.studylib.modules.operation.dto;

import com.studylib.common.dto.PageQueryDTO;

public class RecommendSlotQueryDTO extends PageQueryDTO {

  private String pageCode;
  private String status;

  public String getPageCode() {
    return pageCode;
  }

  public void setPageCode(String pageCode) {
    this.pageCode = pageCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
