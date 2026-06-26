package com.studylib.modules.risk.dto;

import com.studylib.common.dto.PageQueryDTO;

public class ReportQueryDTO extends PageQueryDTO {

  private String keyword;
  private String reportType;
  private String status;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
