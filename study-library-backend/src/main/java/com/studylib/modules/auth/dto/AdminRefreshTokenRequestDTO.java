package com.studylib.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminRefreshTokenRequestDTO {

  @NotBlank(message = "refreshToken is required")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
