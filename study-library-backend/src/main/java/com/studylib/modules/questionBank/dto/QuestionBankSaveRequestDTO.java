package com.studylib.modules.questionBank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionBankSaveRequestDTO {

  private Long id;
  private Long bankId;

  @NotBlank(message = "bankName is required")
  private String bankName;

  @NotBlank(message = "categoryName is required")
  private String categoryName;

  @NotNull(message = "questionCount is required")
  private Integer questionCount;

  @NotBlank(message = "difficulty is required")
  private String difficulty;

  @NotBlank(message = "status is required")
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBankId() {
    return bankId;
  }

  public void setBankId(Long bankId) {
    this.bankId = bankId;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public Integer getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(Integer questionCount) {
    this.questionCount = questionCount;
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
}
