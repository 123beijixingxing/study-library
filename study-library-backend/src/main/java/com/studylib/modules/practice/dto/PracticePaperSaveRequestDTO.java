package com.studylib.modules.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PracticePaperSaveRequestDTO {

  private Long paperId;
  private Long id;

  @NotBlank(message = "paperName is required")
  private String paperName;

  @NotBlank(message = "paperType is required")
  private String paperType;

  @NotBlank(message = "courseName is required")
  private String courseName;

  @NotNull(message = "questionCount is required")
  private Integer questionCount;

  @NotBlank(message = "status is required")
  private String status;

  @NotNull(message = "avgScore is required")
  private Integer avgScore;

  public Long getPaperId() {
    return paperId;
  }

  public void setPaperId(Long paperId) {
    this.paperId = paperId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPaperName() {
    return paperName;
  }

  public void setPaperName(String paperName) {
    this.paperName = paperName;
  }

  public String getPaperType() {
    return paperType;
  }

  public void setPaperType(String paperType) {
    this.paperType = paperType;
  }

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getQuestionCount() {
    return questionCount;
  }

  public void setQuestionCount(Integer questionCount) {
    this.questionCount = questionCount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getAvgScore() {
    return avgScore;
  }

  public void setAvgScore(Integer avgScore) {
    this.avgScore = avgScore;
  }
}
