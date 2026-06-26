package com.studylib.modules.user.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.user.service.AdminUserService;

public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public ApiResponse<PageResponse<Object>> list(Integer pageNum, Integer pageSize, String keyword, String status) {
        return ApiResponse.success(adminUserService.list(pageNum, pageSize, keyword, status));
    }

    public ApiResponse<IdResponseVO> create(Object request) {
        return ApiResponse.success(adminUserService.create(request));
    }
}
