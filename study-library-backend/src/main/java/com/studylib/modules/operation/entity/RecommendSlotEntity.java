package com.studylib.modules.operation.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class RecommendSlotEntity extends BaseAuditEntity {

  private Long slotId;
  private String slotName;
  private String pageCode;
  private String resourceType;
  private String targetTitle;
  private Integer sortNo;
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
