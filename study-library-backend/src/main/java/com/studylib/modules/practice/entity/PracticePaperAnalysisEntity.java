package com.studylib.modules.practice.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class PracticePaperAnalysisEntity extends BaseAuditEntity {

  private Long paperId;
  private Integer totalSubmitCount;
  private Integer passRate;
  private String scoreDistributionJson;
  private String trendJson;
  private String hourlyHeatJson;

  public Long getPaperId() {
    return paperId;
  }

  public void setPaperId(Long paperId) {
    this.paperId = paperId;
  }

  public Integer getTotalSubmitCount() {
    return totalSubmitCount;
  }

  public void setTotalSubmitCount(Integer totalSubmitCount) {
    this.totalSubmitCount = totalSubmitCount;
  }

  public Integer getPassRate() {
    return passRate;
  }

  public void setPassRate(Integer passRate) {
    this.passRate = passRate;
  }

  public String getScoreDistributionJson() {
    return scoreDistributionJson;
  }

  public void setScoreDistributionJson(String scoreDistributionJson) {
    this.scoreDistributionJson = scoreDistributionJson;
  }

  public String getTrendJson() {
    return trendJson;
  }

  public void setTrendJson(String trendJson) {
    this.trendJson = trendJson;
  }

  public String getHourlyHeatJson() {
    return hourlyHeatJson;
  }

  public void setHourlyHeatJson(String hourlyHeatJson) {
    this.hourlyHeatJson = hourlyHeatJson;
  }
}
