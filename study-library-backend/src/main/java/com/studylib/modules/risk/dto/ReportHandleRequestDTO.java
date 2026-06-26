package com.studylib.modules.risk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportHandleRequestDTO {

  private String status;
  private String handleResult;
  private String handleRemark;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getHandleResult() {
    return handleResult;
  }

  public void setHandleResult(String handleResult) {
    this.handleResult = handleResult;
  }

  public String getHandleRemark() {
    return handleRemark;
  }

  public void setHandleRemark(String handleRemark) {
    this.handleRemark = handleRemark;
  }
}
