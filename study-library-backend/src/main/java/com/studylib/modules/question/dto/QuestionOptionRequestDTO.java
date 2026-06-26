package com.studylib.modules.question.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionOptionRequestDTO {

  private String key;
  private String label;
  private Boolean isCorrect;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Boolean getIsCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(Boolean isCorrect) {
    this.isCorrect = isCorrect;
  }
}
