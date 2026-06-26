package com.studylib.modules.member.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class MemberEntity extends BaseAuditEntity {

  private Long id;
  private String username;
  private String memberLevel;
  private LocalDateTime expireTime;
  private Integer renewalCount;
  private String status;

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

  public String getMemberLevel() {
    return memberLevel;
  }

  public void setMemberLevel(String memberLevel) {
    this.memberLevel = memberLevel;
  }

  public LocalDateTime getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(LocalDateTime expireTime) {
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
