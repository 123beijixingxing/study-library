package com.studylib.modules.auth.controller;

import com.studylib.common.util.TokenUtils;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.auth.dto.AdminLoginRequestDTO;
import com.studylib.modules.auth.dto.AdminRefreshTokenRequestDTO;
import com.studylib.modules.auth.service.AdminAuthService;
import com.studylib.modules.auth.vo.AdminLoginResponseVO;
import com.studylib.modules.auth.vo.AdminUserProfileVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/auth")
public class AdminAuthController {

  private final AdminAuthService adminAuthService;

  public AdminAuthController(AdminAuthService adminAuthService) {
    this.adminAuthService = adminAuthService;
  }

  @PostMapping("/login")
  public ApiResponse<AdminLoginResponseVO> login(@Valid @RequestBody AdminLoginRequestDTO request,
      @RequestHeader(value = "User-Agent", required = false) String userAgent) {
    return ApiResponse.success(adminAuthService.login(request, userAgent));
  }

  @PostMapping("/refresh")
  public ApiResponse<AdminLoginResponseVO> refresh(@Valid @RequestBody AdminRefreshTokenRequestDTO request,
      @RequestHeader(value = "User-Agent", required = false) String userAgent) {
    return ApiResponse.success(adminAuthService.refresh(request.getRefreshToken(), userAgent));
  }

  @PostMapping("/logout")
  public ApiResponse<SuccessResponseVO> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
    return ApiResponse.success(adminAuthService.logout(TokenUtils.extractBearerToken(authorization)));
  }

  @GetMapping("/me")
  public ApiResponse<AdminUserProfileVO> getProfile(@RequestHeader(value = "Authorization", required = false) String authorization) {
    return ApiResponse.success(adminAuthService.getProfile(TokenUtils.extractBearerToken(authorization)));
  }
}
