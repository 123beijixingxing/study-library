package com.studylib.modules.log.entity;

import java.time.LocalDateTime;

public class OperationLogEntity {

  private Long id;
  private LocalDateTime operateTime;
  private String operatorName;
  private String operationModule;
  private String operationType;
  private String operationContent;
  private String ip;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getOperateTime() {
    return operateTime;
  }

  public void setOperateTime(LocalDateTime operateTime) {
    this.operateTime = operateTime;
  }

  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public String getOperationModule() {
    return operationModule;
  }

  public void setOperationModule(String operationModule) {
    this.operationModule = operationModule;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public String getOperationContent() {
    return operationContent;
  }

  public void setOperationContent(String operationContent) {
    this.operationContent = operationContent;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }
}
