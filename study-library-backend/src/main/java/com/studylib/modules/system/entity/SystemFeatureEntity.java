package com.studylib.modules.system.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class SystemFeatureEntity extends BaseAuditEntity {

  private String code;
  private String name;
  private Boolean enabled;
  private String description;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
