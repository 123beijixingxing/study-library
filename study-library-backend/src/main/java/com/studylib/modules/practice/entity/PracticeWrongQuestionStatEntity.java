package com.studylib.modules.practice.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class PracticeWrongQuestionStatEntity extends BaseAuditEntity {

  private Long id;
  private Long paperId;
  private Long questionId;
  private String questionTitle;
  private Integer wrongCount;
  private Integer wrongRate;
  private String difficulty;
  private Integer sortNo;

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

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public String getQuestionTitle() {
    return questionTitle;
  }

  public void setQuestionTitle(String questionTitle) {
    this.questionTitle = questionTitle;
  }

  public Integer getWrongCount() {
    return wrongCount;
  }

  public void setWrongCount(Integer wrongCount) {
    this.wrongCount = wrongCount;
  }

  public Integer getWrongRate() {
    return wrongRate;
  }

  public void setWrongRate(Integer wrongRate) {
    this.wrongRate = wrongRate;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }
}
