package com.studylib.modules.log.dto;

import com.studylib.common.dto.PageQueryDTO;

public class OperationLogQueryDTO extends PageQueryDTO {

  private String keyword;
  private String operationType;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }
}
