package com.studylib.modules.user.dto;

import com.studylib.common.dto.PageQueryDTO;

public class AdminUserQueryDTO extends PageQueryDTO {

  private String role;
  private String status;

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
