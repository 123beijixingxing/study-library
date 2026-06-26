package com.studylib.modules.auth.vo;

import java.util.List;

public record AdminUserProfileVO(Long userId, String username, List<String> roleList) {
}
