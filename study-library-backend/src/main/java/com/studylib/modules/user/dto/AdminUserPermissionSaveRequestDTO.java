package com.studylib.modules.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserPermissionSaveRequestDTO {

  @NotEmpty(message = "roleList is required")
  private List<String> roleList;

  @NotEmpty(message = "permissionList is required")
  private List<String> permissionList;

  public List<String> getRoleList() {
    return roleList;
  }

  public void setRoleList(List<String> roleList) {
    this.roleList = roleList;
  }

  public List<String> getPermissionList() {
    return permissionList;
  }

  public void setPermissionList(List<String> permissionList) {
    this.permissionList = permissionList;
  }
}
