package com.studylib.modules.search.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class SearchKeywordEntity extends BaseAuditEntity {

  private Long keywordId;
  private String keyword;
  private String scene;
  private Integer sortNo;
  private String status;
  private String effectTimeRange;
  private String synonymText;

  public Long getKeywordId() {
    return keywordId;
  }

  public void setKeywordId(Long keywordId) {
    this.keywordId = keywordId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getScene() {
    return scene;
  }

  public void setScene(String scene) {
    this.scene = scene;
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

  public String getEffectTimeRange() {
    return effectTimeRange;
  }

  public void setEffectTimeRange(String effectTimeRange) {
    this.effectTimeRange = effectTimeRange;
  }

  public String getSynonymText() {
    return synonymText;
  }

  public void setSynonymText(String synonymText) {
    this.synonymText = synonymText;
  }
}
