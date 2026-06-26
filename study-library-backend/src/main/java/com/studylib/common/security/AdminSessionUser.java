package com.studylib.common.security;

import java.util.List;

public record AdminSessionUser(Long userId, String username, List<String> roleList, List<String> permissionList) {
}
