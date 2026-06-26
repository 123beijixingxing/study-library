package com.studylib.modules.operation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecommendSlotSaveRequestDTO {

  private Long slotId;

  @NotBlank(message = "slotName is required")
  private String slotName;

  @NotBlank(message = "pageCode is required")
  private String pageCode;

  @NotBlank(message = "resourceType is required")
  private String resourceType;

  @NotBlank(message = "targetTitle is required")
  private String targetTitle;

  @NotNull(message = "sortNo is required")
  private Integer sortNo;

  @NotBlank(message = "status is required")
  private String status;

  public Long getSlotId() {
    return slotId;
  }

  public void setSlotId(Long slotId) {
    this.slotId = slotId;
  }

  public String getSlotName() {
    return slotName;
  }

  public void setSlotName(String slotName) {
    this.slotName = slotName;
  }

  public String getPageCode() {
    return pageCode;
  }

  public void setPageCode(String pageCode) {
    this.pageCode = pageCode;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public String getTargetTitle() {
    return targetTitle;
  }

  public void setTargetTitle(String targetTitle) {
    this.targetTitle = targetTitle;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
