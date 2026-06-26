package com.studylib.common.dto;

import jakarta.validation.constraints.Min;

public class PageQueryDTO {

  @Min(value = 1, message = "pageNum must be greater than 0")
  private Integer pageNum = 1;

  @Min(value = 1, message = "pageSize must be greater than 0")
  private Integer pageSize = 10;

  private String keyword;

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public int safePageNum() {
    return pageNum == null || pageNum < 1 ? 1 : pageNum;
  }

  public int safePageSize() {
    return pageSize == null || pageSize < 1 ? 10 : pageSize;
  }
}
