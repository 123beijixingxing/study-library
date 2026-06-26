package com.studylib.modules.auth.service.impl;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.auth.dto.AppLoginRequestDTO;
import com.studylib.modules.auth.dto.AppRegisterRequestDTO;
import com.studylib.modules.auth.mapper.AuthSessionMapper;
import com.studylib.modules.auth.mapper.UserMapper;
import com.studylib.modules.auth.service.AppAuthService;
import com.studylib.modules.auth.vo.AppLoginResponseVO;

public class AppAuthServiceImpl implements AppAuthService {
    private final UserMapper userMapper;
    private final AuthSessionMapper authSessionMapper;

    public AppAuthServiceImpl(UserMapper userMapper, AuthSessionMapper authSessionMapper) {
        this.userMapper = userMapper;
        this.authSessionMapper = authSessionMapper;
    }

    @Override
    public AppLoginResponseVO login(AppLoginRequestDTO request) {
        throw new UnsupportedOperationException("Implement login flow with userMapper and authSessionMapper");
    }

    @Override
    public IdResponseVO register(AppRegisterRequestDTO request) {
        throw new UnsupportedOperationException("Implement register flow with uniqueness validation");
    }
}
