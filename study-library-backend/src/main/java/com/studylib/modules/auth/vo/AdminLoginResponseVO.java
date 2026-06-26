package com.studylib.modules.auth.vo;

public record AdminLoginResponseVO(
    String token,
    String refreshToken,
    Long expiresIn,
    Long refreshExpiresIn,
    AdminUserProfileVO userInfo
) {
}
