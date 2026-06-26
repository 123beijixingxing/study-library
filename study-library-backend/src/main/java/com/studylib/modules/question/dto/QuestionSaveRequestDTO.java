package com.studylib.modules.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionSaveRequestDTO {

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

  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "questionType is required")
  private String questionType;

  @NotBlank(message = "difficulty is required")
  private String difficulty;

  @NotBlank(message = "status is required")
  private String status;

  @Valid
  private List<QuestionOptionRequestDTO> optionList = new ArrayList<>();

  private String answerText;
  private String analysisText;

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

  public List<QuestionOptionRequestDTO> getOptionList() {
    return optionList;
  }

  public void setOptionList(List<QuestionOptionRequestDTO> optionList) {
    this.optionList = optionList == null ? new ArrayList<>() : optionList;
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
}
