package com.studylib.common.security;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminPermissionInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod handlerMethod)) {
      return true;
    }

    RequirePermission requirePermission = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), RequirePermission.class);
    if (requirePermission == null) {
      requirePermission = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequirePermission.class);
    }
    if (requirePermission == null) {
      return true;
    }

    AdminSessionUser currentUser = AdminRequestContext.getCurrentUser()
        .orElseThrow(() -> new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Login has expired"));
    if (!currentUser.permissionList().contains(requirePermission.value())) {
      throw new BusinessException(ErrorCodeConstants.FORBIDDEN, "Insufficient permission: " + requirePermission.value());
    }
    return true;
  }
}
