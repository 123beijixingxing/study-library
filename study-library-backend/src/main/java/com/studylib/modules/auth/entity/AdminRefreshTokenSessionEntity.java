package com.studylib.modules.auth.entity;

public class AdminRefreshTokenSessionEntity {

  private String sessionId;
  private Long userId;
  private String refreshToken;
  private Long expiresAt;
  private Boolean revoked;
  private String userAgent;
  private Long lastUsedAt;
  private Long createdAt;
  private Long updatedAt;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Long expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Boolean getRevoked() {
    return revoked;
  }

  public void setRevoked(Boolean revoked) {
    this.revoked = revoked;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public Long getLastUsedAt() {
    return lastUsedAt;
  }

  public void setLastUsedAt(Long lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }
}
