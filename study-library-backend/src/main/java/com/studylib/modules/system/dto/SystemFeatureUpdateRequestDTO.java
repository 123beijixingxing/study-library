package com.studylib.modules.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemFeatureUpdateRequestDTO {

  private String code;
  private String featureCode;
  private String name;
  private String featureName;

  @NotNull(message = "enabled is required")
  private Boolean enabled;

  private String description;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFeatureCode() {
    return featureCode;
  }

  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFeatureName() {
    return featureName;
  }

  public void setFeatureName(String featureName) {
    this.featureName = featureName;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
