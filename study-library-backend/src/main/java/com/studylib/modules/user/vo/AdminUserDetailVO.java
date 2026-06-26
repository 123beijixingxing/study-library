package com.studylib.modules.user.vo;

import java.time.LocalDateTime;

public record AdminUserDetailVO(
    Long id,
    String username,
    String email,
    String phone,
    String role,
    String status,
    LocalDateTime registerTime,
    String memberLevel,
    Integer studyRecordCount,
    Integer favoriteCount,
    LocalDateTime lastLoginTime
) {
}
