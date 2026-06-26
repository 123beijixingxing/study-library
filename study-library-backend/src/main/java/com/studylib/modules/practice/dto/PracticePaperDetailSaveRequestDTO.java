package com.studylib.modules.practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studylib.modules.question.dto.QuestionSaveRequestDTO;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PracticePaperDetailSaveRequestDTO {

  private Long paperId;
  private String paperName;
  private String paperType;
  private String courseName;
  private String ruleDesc;

  @Valid
  private List<QuestionSaveRequestDTO> questionList = new ArrayList<>();

  public Long getPaperId() {
    return paperId;
  }

  public void setPaperId(Long paperId) {
    this.paperId = paperId;
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

  public String getRuleDesc() {
    return ruleDesc;
  }

  public void setRuleDesc(String ruleDesc) {
    this.ruleDesc = ruleDesc;
  }

  public List<QuestionSaveRequestDTO> getQuestionList() {
    return questionList;
  }

  public void setQuestionList(List<QuestionSaveRequestDTO> questionList) {
    this.questionList = questionList == null ? new ArrayList<>() : questionList;
  }
}
