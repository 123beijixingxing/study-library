package com.studylib.modules.practice.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class PracticePaperQuestionEntity extends BaseAuditEntity {

  private Long id;
  private Long paperId;
  private Integer sortNo;
  private Long questionId;
  private Long bankId;
  private String chapterName;
  private String templateCode;
  private String versionGroupId;
  private Integer versionNo;
  private String versionRemark;
  private String sourceAction;
  private Long sourceQuestionId;
  private Integer sourceVersionNo;
  private String title;
  private String questionType;
  private String difficulty;
  private String status;
  private String answerText;
  private String analysisText;
  private String optionJson;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPaperId() {
    return paperId;
  }

  public void setPaperId(Long paperId) {
    this.paperId = paperId;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public Long getBankId() {
    return bankId;
  }

  public void setBankId(Long bankId) {
    this.bankId = bankId;
  }

  public String getChapterName() {
    return chapterName;
  }

  public void setChapterName(String chapterName) {
    this.chapterName = chapterName;
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

  public Integer getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(Integer versionNo) {
    this.versionNo = versionNo;
  }

  public String getVersionRemark() {
    return versionRemark;
  }

  public void setVersionRemark(String versionRemark) {
    this.versionRemark = versionRemark;
  }

  public String getSourceAction() {
    return sourceAction;
  }

  public void setSourceAction(String sourceAction) {
    this.sourceAction = sourceAction;
  }

  public Long getSourceQuestionId() {
    return sourceQuestionId;
  }

  public void setSourceQuestionId(Long sourceQuestionId) {
    this.sourceQuestionId = sourceQuestionId;
  }

  public Integer getSourceVersionNo() {
    return sourceVersionNo;
  }

  public void setSourceVersionNo(Integer sourceVersionNo) {
    this.sourceVersionNo = sourceVersionNo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public String getAnalysisText() {
    return analysisText;
  }

  public void setAnalysisText(String analysisText) {
    this.analysisText = analysisText;
  }

  public String getOptionJson() {
    return optionJson;
  }

  public void setOptionJson(String optionJson) {
    this.optionJson = optionJson;
  }
}
