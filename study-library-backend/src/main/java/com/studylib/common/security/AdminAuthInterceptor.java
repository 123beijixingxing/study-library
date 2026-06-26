package com.studylib.common.security;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.util.TokenUtils;
import com.studylib.modules.auth.entity.AdminRefreshTokenSessionEntity;
import com.studylib.modules.auth.mapper.AdminRefreshTokenSessionMapper;
import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.entity.UserPermissionEntity;
import com.studylib.modules.user.mapper.AdminUserMapper;
import com.studylib.modules.user.mapper.AdminUserPermissionMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

  private final JwtTokenService jwtTokenService;
  private final AdminRefreshTokenSessionMapper sessionMapper;
  private final AdminUserMapper adminUserMapper;
  private final AdminUserPermissionMapper adminUserPermissionMapper;

  public AdminAuthInterceptor(JwtTokenService jwtTokenService, AdminRefreshTokenSessionMapper sessionMapper,
      AdminUserMapper adminUserMapper, AdminUserPermissionMapper adminUserPermissionMapper) {
    this.jwtTokenService = jwtTokenService;
    this.sessionMapper = sessionMapper;
    this.adminUserMapper = adminUserMapper;
    this.adminUserPermissionMapper = adminUserPermissionMapper;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    if (isPublicPath(request.getRequestURI())) {
      return true;
    }

    String token = TokenUtils.extractBearerToken(request.getHeader("Authorization"));
    if (token.isBlank()) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Missing access token");
    }

    JwtTokenPayload payload = jwtTokenService.parseAccessToken(token);
    AdminRefreshTokenSessionEntity session = sessionMapper.selectBySessionId(payload.sessionId());
    if (session == null || Boolean.TRUE.equals(session.getRevoked())) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Login session has expired");
    }
    if (session.getExpiresAt() == null || session.getExpiresAt() <= Instant.now().toEpochMilli()) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Refresh token has expired");
    }

    AdminUserEntity user = adminUserMapper.selectById(payload.userId());
    if (user == null) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Current account does not exist");
    }
    if (!"enabled".equalsIgnoreCase(user.getStatus())) {
      throw new BusinessException(ErrorCodeConstants.FORBIDDEN, "Current account is disabled");
    }

    UserPermissionEntity permission = adminUserPermissionMapper.selectByUserId(payload.userId());
    List<String> roleList = permission == null || permission.getRoleList().isEmpty() ? List.of(user.getRole()) : permission.getRoleList();
    List<String> permissionList = permission == null ? List.of() : permission.getPermissionList();

    AdminRequestContext.setCurrentUser(
        request,
        new AdminSessionUser(user.getId(), user.getUsername(), new ArrayList<>(roleList), new ArrayList<>(permissionList))
    );
    return true;
  }

  private boolean isPublicPath(String requestUri) {
    return "/api/admin/v1/auth/login".equals(requestUri)
        || "/api/admin/v1/auth/refresh".equals(requestUri);
  }
}
