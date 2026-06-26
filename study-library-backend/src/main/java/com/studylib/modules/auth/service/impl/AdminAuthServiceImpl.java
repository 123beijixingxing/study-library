package com.studylib.modules.auth.service.impl;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.security.JwtTokenPayload;
import com.studylib.common.security.JwtTokenService;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.auth.dto.AdminLoginRequestDTO;
import com.studylib.modules.auth.entity.AdminRefreshTokenSessionEntity;
import com.studylib.modules.auth.mapper.AdminRefreshTokenSessionMapper;
import com.studylib.modules.auth.service.AdminAuthService;
import com.studylib.modules.auth.vo.AdminLoginResponseVO;
import com.studylib.modules.auth.vo.AdminUserProfileVO;
import com.studylib.modules.user.entity.AdminUserEntity;
import com.studylib.modules.user.entity.UserPermissionEntity;
import com.studylib.modules.user.mapper.AdminUserMapper;
import com.studylib.modules.user.mapper.AdminUserPermissionMapper;
import com.studylib.modules.user.service.AdminUserService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

  private final AuditLogRepository auditLogRepository;
  private final AdminRefreshTokenSessionMapper sessionMapper;
  private final AdminUserMapper adminUserMapper;
  private final AdminUserPermissionMapper adminUserPermissionMapper;
  private final AdminUserService adminUserService;
  private final JwtTokenService jwtTokenService;

  public AdminAuthServiceImpl(AuditLogRepository auditLogRepository, AdminRefreshTokenSessionMapper sessionMapper,
      AdminUserMapper adminUserMapper, AdminUserPermissionMapper adminUserPermissionMapper, AdminUserService adminUserService,
      JwtTokenService jwtTokenService) {
    this.auditLogRepository = auditLogRepository;
    this.sessionMapper = sessionMapper;
    this.adminUserMapper = adminUserMapper;
    this.adminUserPermissionMapper = adminUserPermissionMapper;
    this.adminUserService = adminUserService;
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  public AdminLoginResponseVO login(AdminLoginRequestDTO request, String userAgent) {
    String username = request.getUsername().trim();
    AdminUserEntity user = adminUserService.findByUsername(username)
        .orElseThrow(() -> buildLoginFailure(username, "Username or password is incorrect"));
    if (!user.getPassword().equals(request.getPassword().trim())) {
      throw buildLoginFailure(username, "Username or password is incorrect");
    }
    if (!"enabled".equalsIgnoreCase(user.getStatus())) {
      auditLogRepository.appendLoginLog(username, "fail");
      throw new BusinessException(ErrorCodeConstants.FORBIDDEN, "Current account is disabled");
    }

    user.setLastLoginTime(DateTimeUtils.now());
    adminUserMapper.update(user);
    AdminUserEntity savedUser = adminUserMapper.selectById(user.getId());
    String sessionId = UUID.randomUUID().toString().replace("-", "");
    String accessToken = jwtTokenService.createAccessToken(savedUser, sessionId);
    String refreshToken = jwtTokenService.createRefreshToken(savedUser, sessionId);
    long nowMillis = Instant.now().toEpochMilli();
    AdminRefreshTokenSessionEntity session = buildSession(savedUser.getId(), sessionId, refreshToken, userAgent, nowMillis);
    sessionMapper.insert(session);
    auditLogRepository.appendLoginLog(savedUser.getUsername(), "success");
    return buildLoginResponse(savedUser, refreshToken, accessToken);
  }

  @Override
  public AdminLoginResponseVO refresh(String refreshToken, String userAgent) {
    JwtTokenPayload payload = jwtTokenService.parseRefreshToken(refreshToken);
    AdminRefreshTokenSessionEntity session = getActiveSession(payload.sessionId());
    if (!session.getUserId().equals(payload.userId()) || !refreshToken.equals(session.getRefreshToken())) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Refresh token is invalid");
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

    String newAccessToken = jwtTokenService.createAccessToken(user, session.getSessionId());
    String newRefreshToken = jwtTokenService.createRefreshToken(user, session.getSessionId());
    long nowMillis = Instant.now().toEpochMilli();
    session.setRefreshToken(newRefreshToken);
    session.setExpiresAt(nowMillis + jwtTokenService.getRefreshExpiresInSeconds() * 1000);
    session.setRevoked(Boolean.FALSE);
    session.setLastUsedAt(nowMillis);
    session.setUpdatedAt(nowMillis);
    session.setUserAgent(userAgent);
    sessionMapper.update(session);
    return buildLoginResponse(user, newRefreshToken, newAccessToken);
  }

  @Override
  public AdminUserProfileVO getProfile(String accessToken) {
    JwtTokenPayload payload = jwtTokenService.parseAccessToken(accessToken);
    getActiveSession(payload.sessionId());
    AdminUserEntity user = adminUserService.findById(payload.userId())
        .orElseThrow(() -> new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Current account does not exist"));
    return buildProfile(user);
  }

  @Override
  public SuccessResponseVO logout(String accessToken) {
    if (accessToken != null && !accessToken.isBlank()) {
      jwtTokenService.tryParseAccessToken(accessToken).ifPresent(payload -> {
        sessionMapper.revokeBySessionId(payload.sessionId(), Instant.now().toEpochMilli());
        adminUserService.findById(payload.userId())
            .ifPresent(user -> auditLogRepository.appendLoginLog(user.getUsername(), "logout"));
      });
    }
    return SuccessResponseVO.ok();
  }

  private BusinessException buildLoginFailure(String username, String message) {
    auditLogRepository.appendLoginLog(username, "fail");
    return new BusinessException(ErrorCodeConstants.UNAUTHORIZED, message);
  }

  private AdminUserProfileVO buildProfile(AdminUserEntity user) {
    UserPermissionEntity permission = adminUserPermissionMapper.selectByUserId(user.getId());
    List<String> roleList = permission == null || permission.getRoleList().isEmpty() ? List.of(user.getRole()) : permission.getRoleList();
    return new AdminUserProfileVO(user.getId(), user.getUsername(), new ArrayList<>(roleList));
  }

  private AdminLoginResponseVO buildLoginResponse(AdminUserEntity user, String refreshToken, String accessToken) {
    return new AdminLoginResponseVO(
        accessToken,
        refreshToken,
        jwtTokenService.getAccessExpiresInSeconds(),
        jwtTokenService.getRefreshExpiresInSeconds(),
        buildProfile(user)
    );
  }

  private AdminRefreshTokenSessionEntity buildSession(Long userId, String sessionId, String refreshToken, String userAgent, long nowMillis) {
    AdminRefreshTokenSessionEntity entity = new AdminRefreshTokenSessionEntity();
    entity.setSessionId(sessionId);
    entity.setUserId(userId);
    entity.setRefreshToken(refreshToken);
    entity.setExpiresAt(nowMillis + jwtTokenService.getRefreshExpiresInSeconds() * 1000);
    entity.setRevoked(Boolean.FALSE);
    entity.setUserAgent(userAgent);
    entity.setLastUsedAt(nowMillis);
    entity.setCreatedAt(nowMillis);
    entity.setUpdatedAt(nowMillis);
    return entity;
  }

  private AdminRefreshTokenSessionEntity getActiveSession(String sessionId) {
    AdminRefreshTokenSessionEntity session = sessionMapper.selectBySessionId(sessionId);
    if (session == null || Boolean.TRUE.equals(session.getRevoked())) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Login session has expired");
    }
    if (session.getExpiresAt() == null || session.getExpiresAt() <= Instant.now().toEpochMilli()) {
      throw new BusinessException(ErrorCodeConstants.UNAUTHORIZED, "Refresh token has expired");
    }
    return session;
  }
}
