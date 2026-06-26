package com.studylib.modules.search.dto;

import com.studylib.common.dto.PageQueryDTO;

public class SearchKeywordQueryDTO extends PageQueryDTO {

  private String keyword;
  private String scene;
  private String status;

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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
