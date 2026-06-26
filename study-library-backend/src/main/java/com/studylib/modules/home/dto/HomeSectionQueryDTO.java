package com.studylib.modules.home.dto;

import com.studylib.common.dto.PageQueryDTO;

public class HomeSectionQueryDTO extends PageQueryDTO {

  private String sectionType;

  public String getSectionType() {
    return sectionType;
  }

  public void setSectionType(String sectionType) {
    this.sectionType = sectionType;
  }
}
