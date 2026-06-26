package com.studylib.modules.practice.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class PracticePaperEntity extends BaseAuditEntity {

  private Long paperId;
  private String paperName;
  private String paperType;
  private String courseName;
  private String ruleDesc;
  private Integer questionCount;
  private String status;
  private Integer avgScore;
  private LocalDateTime updateTime;

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

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
