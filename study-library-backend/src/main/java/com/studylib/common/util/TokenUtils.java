package com.studylib.common.util;

public final class TokenUtils {

  private TokenUtils() {
  }

  public static String extractBearerToken(String authorization) {
    if (authorization == null || authorization.isBlank()) {
      return "";
    }
    if (authorization.startsWith("Bearer ")) {
      return authorization.substring(7).trim();
    }
    return authorization.trim();
  }
}
