package com.studylib.modules.log.entity;

import java.time.LocalDateTime;

public class LoginLogEntity {

  private Long id;
  private LocalDateTime loginTime;
  private String username;
  private String loginType;
  private String status;
  private String ip;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(LocalDateTime loginTime) {
    this.loginTime = loginTime;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }
}
