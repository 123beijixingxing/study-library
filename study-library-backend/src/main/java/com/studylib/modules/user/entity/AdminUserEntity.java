package com.studylib.modules.user.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class AdminUserEntity extends BaseAuditEntity {

  private Long id;
  private String username;
  private String password;
  private String email;
  private String phone;
  private String role;
  private String status;
  private LocalDateTime registerTime;
  private String memberLevel;
  private Integer studyRecordCount;
  private Integer favoriteCount;
  private LocalDateTime lastLoginTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

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

  public LocalDateTime getRegisterTime() {
    return registerTime;
  }

  public void setRegisterTime(LocalDateTime registerTime) {
    this.registerTime = registerTime;
  }

  public String getMemberLevel() {
    return memberLevel;
  }

  public void setMemberLevel(String memberLevel) {
    this.memberLevel = memberLevel;
  }

  public Integer getStudyRecordCount() {
    return studyRecordCount;
  }

  public void setStudyRecordCount(Integer studyRecordCount) {
    this.studyRecordCount = studyRecordCount;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount(Integer favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public LocalDateTime getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(LocalDateTime lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }
}
