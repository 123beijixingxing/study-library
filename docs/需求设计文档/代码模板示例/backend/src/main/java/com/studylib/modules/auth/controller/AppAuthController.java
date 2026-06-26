package com.studylib.modules.auth.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.auth.dto.AppLoginRequestDTO;
import com.studylib.modules.auth.dto.AppRegisterRequestDTO;
import com.studylib.modules.auth.service.AppAuthService;
import com.studylib.modules.auth.vo.AppLoginResponseVO;

public class AppAuthController {
    private final AppAuthService appAuthService;

    public AppAuthController(AppAuthService appAuthService) {
        this.appAuthService = appAuthService;
    }

    public ApiResponse<AppLoginResponseVO> login(AppLoginRequestDTO request) {
        return ApiResponse.success(appAuthService.login(request));
    }

    public ApiResponse<IdResponseVO> register(AppRegisterRequestDTO request) {
        return ApiResponse.success(appAuthService.register(request));
    }
}
