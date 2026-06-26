package com.studylib.modules.user.vo;

import java.util.List;

public record AdminUserPermissionVO(Long userId, List<String> roleList, List<String> permissionList) {
}
