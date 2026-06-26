package com.studylib.modules.garden.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GardenAuditRequestDTO {

  @NotBlank(message = "auditStatus is required")
  private String auditStatus;

  public String getAuditStatus() {
    return auditStatus;
  }

  public void setAuditStatus(String auditStatus) {
    this.auditStatus = auditStatus;
  }
}
