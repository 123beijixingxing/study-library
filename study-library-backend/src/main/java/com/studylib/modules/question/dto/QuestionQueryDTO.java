package com.studylib.modules.question.dto;

public class QuestionQueryDTO {

  private Long bankId;
  private String keyword;
  private String questionType;
  private String status;
  private String templateCode;
  private String versionGroupId;

  public Long getBankId() {
    return bankId;
  }

  public void setBankId(Long bankId) {
    this.bankId = bankId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTemplateCode() {
    return templateCode;
  }

  public void setTemplateCode(String templateCode) {
    this.templateCode = templateCode;
  }

  public String getVersionGroupId() {
    return versionGroupId;
  }

  public void setVersionGroupId(String versionGroupId) {
    this.versionGroupId = versionGroupId;
  }
}
