package com.studylib.modules.auth.service;

import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.auth.dto.AdminLoginRequestDTO;
import com.studylib.modules.auth.vo.AdminLoginResponseVO;
import com.studylib.modules.auth.vo.AdminUserProfileVO;

public interface AdminAuthService {

  AdminLoginResponseVO login(AdminLoginRequestDTO request, String userAgent);

  AdminLoginResponseVO refresh(String refreshToken, String userAgent);

  AdminUserProfileVO getProfile(String accessToken);

  SuccessResponseVO logout(String accessToken);
}
