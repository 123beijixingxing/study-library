package com.studylib.modules.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchKeywordSaveRequestDTO {

  private Long keywordId;

  @NotBlank(message = "keyword is required")
  private String keyword;

  @NotBlank(message = "scene is required")
  private String scene;

  @NotNull(message = "sortNo is required")
  private Integer sortNo;

  @NotBlank(message = "status is required")
  private String status;

  @NotBlank(message = "effectTimeRange is required")
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
