package com.studylib.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

  private String issuer;
  private String secret;
  private Long accessTokenExpireSeconds;
  private Long refreshTokenExpireSeconds;

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Long getAccessTokenExpireSeconds() {
    return accessTokenExpireSeconds;
  }

  public void setAccessTokenExpireSeconds(Long accessTokenExpireSeconds) {
    this.accessTokenExpireSeconds = accessTokenExpireSeconds;
  }

  public Long getRefreshTokenExpireSeconds() {
    return refreshTokenExpireSeconds;
  }

  public void setRefreshTokenExpireSeconds(Long refreshTokenExpireSeconds) {
    this.refreshTokenExpireSeconds = refreshTokenExpireSeconds;
  }
}
