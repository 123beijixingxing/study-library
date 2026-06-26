package com.studylib.common.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

  private List<String> allowedOriginPatterns = new ArrayList<>(List.of(
      "http://localhost:*",
      "http://127.0.0.1:*"
  ));

  public List<String> getAllowedOriginPatterns() {
    return allowedOriginPatterns;
  }

  public void setAllowedOriginPatterns(List<String> allowedOriginPatterns) {
    this.allowedOriginPatterns = allowedOriginPatterns == null ? new ArrayList<>() : allowedOriginPatterns;
  }
}
