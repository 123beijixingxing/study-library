package com.studylib.common.config;

import com.studylib.common.security.AdminAuthInterceptor;
import com.studylib.common.security.AdminPermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final CorsProperties corsProperties;
  private final AdminAuthInterceptor adminAuthInterceptor;
  private final AdminPermissionInterceptor adminPermissionInterceptor;

  public WebMvcConfig(
      CorsProperties corsProperties,
      AdminAuthInterceptor adminAuthInterceptor,
      AdminPermissionInterceptor adminPermissionInterceptor
  ) {
    this.corsProperties = corsProperties;
    this.adminAuthInterceptor = adminAuthInterceptor;
    this.adminPermissionInterceptor = adminPermissionInterceptor;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOriginPatterns(corsProperties.getAllowedOriginPatterns().toArray(String[]::new))
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false)
        .maxAge(3600);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(adminAuthInterceptor).addPathPatterns("/api/admin/v1/**");
    registry.addInterceptor(adminPermissionInterceptor).addPathPatterns("/api/admin/v1/**");
  }
}
