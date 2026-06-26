package com.studylib.modules.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GrowthRuleSaveRequestDTO {

  @NotNull(message = "ruleId is required")
  private Long ruleId;

  @NotBlank(message = "ruleName is required")
  private String ruleName;

  @NotBlank(message = "triggerType is required")
  private String triggerType;

  @NotNull(message = "growthValue is required")
  private Integer growthValue;

  @NotNull(message = "dailyLimit is required")
  private Integer dailyLimit;

  @NotBlank(message = "status is required")
  private String status;

  public Long getRuleId() {
    return ruleId;
  }

  public void setRuleId(Long ruleId) {
    this.ruleId = ruleId;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public String getTriggerType() {
    return triggerType;
  }

  public void setTriggerType(String triggerType) {
    this.triggerType = triggerType;
  }

  public Integer getGrowthValue() {
    return growthValue;
  }

  public void setGrowthValue(Integer growthValue) {
    this.growthValue = growthValue;
  }

  public Integer getDailyLimit() {
    return dailyLimit;
  }

  public void setDailyLimit(Integer dailyLimit) {
    this.dailyLimit = dailyLimit;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
