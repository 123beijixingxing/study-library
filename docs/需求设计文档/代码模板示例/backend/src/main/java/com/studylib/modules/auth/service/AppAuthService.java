package com.studylib.modules.auth.service;

import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.auth.dto.AppLoginRequestDTO;
import com.studylib.modules.auth.dto.AppRegisterRequestDTO;
import com.studylib.modules.auth.vo.AppLoginResponseVO;

public interface AppAuthService {
    AppLoginResponseVO login(AppLoginRequestDTO request);

    IdResponseVO register(AppRegisterRequestDTO request);
}
