package com.studylib.common.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class AdminRequestContext {

  public static final String CURRENT_USER_ATTRIBUTE = "CURRENT_ADMIN_USER";

  private AdminRequestContext() {
  }

  public static void setCurrentUser(HttpServletRequest request, AdminSessionUser currentUser) {
    request.setAttribute(CURRENT_USER_ATTRIBUTE, currentUser);
  }

  public static Optional<AdminSessionUser> getCurrentUser() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    if (!(attributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      return Optional.empty();
    }
    Object currentUser = servletRequestAttributes.getRequest().getAttribute(CURRENT_USER_ATTRIBUTE);
    return currentUser instanceof AdminSessionUser adminSessionUser ? Optional.of(adminSessionUser) : Optional.empty();
  }

  public static String getCurrentUsername() {
    return getCurrentUser().map(AdminSessionUser::username).orElse("system");
  }

  public static String getCurrentRequestIp() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    if (!(attributes instanceof ServletRequestAttributes servletRequestAttributes)) {
      return "127.0.0.1";
    }
    HttpServletRequest request = servletRequestAttributes.getRequest();
    String forwardedFor = request.getHeader("X-Forwarded-For");
    if (forwardedFor != null && !forwardedFor.isBlank()) {
      return forwardedFor.split(",")[0].trim();
    }
    String realIp = request.getHeader("X-Real-IP");
    if (realIp != null && !realIp.isBlank()) {
      return realIp.trim();
    }
    return request.getRemoteAddr() == null || request.getRemoteAddr().isBlank() ? "127.0.0.1" : request.getRemoteAddr();
  }
}
