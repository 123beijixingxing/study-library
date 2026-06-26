package com.studylib.modules.content.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.IdResponseVO;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.content.service.AdminContentService;

public class AdminContentController {
    private final AdminContentService adminContentService;

    public AdminContentController(AdminContentService adminContentService) {
        this.adminContentService = adminContentService;
    }

    public ApiResponse<PageResponse<Object>> list(Integer pageNum, Integer pageSize, String contentType, String auditStatus) {
        return ApiResponse.success(adminContentService.list(pageNum, pageSize, contentType, auditStatus));
    }

    public ApiResponse<IdResponseVO> create(Object request) {
        return ApiResponse.success(adminContentService.create(request));
    }

    public ApiResponse<SuccessResponseVO> audit(Long contentId, Object request) {
        return ApiResponse.success(adminContentService.audit(contentId, request));
    }
}
