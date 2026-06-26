package com.studylib.modules.garden.dto;

import com.studylib.common.dto.PageQueryDTO;

public class GardenContentQueryDTO extends PageQueryDTO {

  private String contentType;
  private String auditStatus;

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(String auditStatus) {
    this.auditStatus = auditStatus;
  }
}
