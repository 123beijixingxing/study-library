package com.studylib.modules.risk.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class ReportEntity extends BaseAuditEntity {

  private Long reportId;
  private String reportType;
  private Long targetId;
  private String targetTitle;
  private String reporterName;
  private String status;
  private LocalDateTime reportTime;
  private String handleResult;
  private String handleRemark;

  public Long getReportId() {
    return reportId;
  }

  public void setReportId(Long reportId) {
    this.reportId = reportId;
  }

  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  public Long getTargetId() {
    return targetId;
  }

  public void setTargetId(Long targetId) {
    this.targetId = targetId;
  }

  public String getTargetTitle() {
    return targetTitle;
  }

  public void setTargetTitle(String targetTitle) {
    this.targetTitle = targetTitle;
  }

  public String getReporterName() {
    return reporterName;
  }

  public void setReporterName(String reporterName) {
    this.reporterName = reporterName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getReportTime() {
    return reportTime;
  }

  public void setReportTime(LocalDateTime reportTime) {
    this.reportTime = reportTime;
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
