package com.studylib.modules.question.entity;

public class QuestionOptionEntity {

  private Long id;
  private Long questionId;
  private String optionKey;
  private String optionLabel;
  private Boolean correct;
  private Integer sortNo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Long questionId) {
    this.questionId = questionId;
  }

  public String getOptionKey() {
    return optionKey;
  }

  public void setOptionKey(String optionKey) {
    this.optionKey = optionKey;
  }

  public String getOptionLabel() {
    return optionLabel;
  }

  public void setOptionLabel(String optionLabel) {
    this.optionLabel = optionLabel;
  }

  public Boolean getCorrect() {
    return correct;
  }

  public void setCorrect(Boolean correct) {
    this.correct = correct;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }
}
