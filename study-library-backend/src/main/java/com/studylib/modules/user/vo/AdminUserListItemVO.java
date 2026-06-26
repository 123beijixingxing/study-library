package com.studylib.modules.user.vo;

import java.time.LocalDateTime;

public record AdminUserListItemVO(Long id, String username, String email, String phone, String role, String status, LocalDateTime registerTime) {
}
