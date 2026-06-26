package com.studylib.modules.user.entity;

import java.util.ArrayList;
import java.util.List;

public class UserPermissionEntity {

  private Long userId;
  private List<String> roleList = new ArrayList<>();
  private List<String> permissionList = new ArrayList<>();

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

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
