package com.studylib.modules.home.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class HomeSectionEntity extends BaseAuditEntity {

  private Long id;
  private String sectionType;
  private String title;
  private String summary;
  private Integer hotScore;
  private Integer sortNo;
  private String displayStatus;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSectionType() {
    return sectionType;
  }

  public void setSectionType(String sectionType) {
    this.sectionType = sectionType;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Integer getHotScore() {
    return hotScore;
  }

  public void setHotScore(Integer hotScore) {
    this.hotScore = hotScore;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }

  public String getDisplayStatus() {
    return displayStatus;
  }

  public void setDisplayStatus(String displayStatus) {
    this.displayStatus = displayStatus;
  }
}
