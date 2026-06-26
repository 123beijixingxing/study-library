package com.studylib.modules.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminMemberUpdateRequestDTO {

  @NotNull(message = "userId is required")
  private Long userId;

  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "memberLevel is required")
  private String memberLevel;

  @NotBlank(message = "expireTime is required")
  private String expireTime;

  @NotNull(message = "renewalCount is required")
  private Integer renewalCount;

  @NotBlank(message = "status is required")
  private String status;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getMemberLevel() {
    return memberLevel;
  }

  public void setMemberLevel(String memberLevel) {
    this.memberLevel = memberLevel;
  }

  public String getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  public Integer getRenewalCount() {
    return renewalCount;
  }

  public void setRenewalCount(Integer renewalCount) {
    this.renewalCount = renewalCount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
