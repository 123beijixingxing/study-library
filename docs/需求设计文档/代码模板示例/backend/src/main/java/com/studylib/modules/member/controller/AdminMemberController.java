package com.studylib.modules.member.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.member.service.AdminMemberService;

public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    public AdminMemberController(AdminMemberService adminMemberService) {
        this.adminMemberService = adminMemberService;
    }

    public ApiResponse<PageResponse<Object>> list(Integer pageNum, Integer pageSize, String keyword, String status) {
        return ApiResponse.success(adminMemberService.list(pageNum, pageSize, keyword, status));
    }

    public ApiResponse<SuccessResponseVO> cancel(Long userId, String cancelReason) {
        return ApiResponse.success(adminMemberService.cancel(userId, cancelReason));
    }
}
