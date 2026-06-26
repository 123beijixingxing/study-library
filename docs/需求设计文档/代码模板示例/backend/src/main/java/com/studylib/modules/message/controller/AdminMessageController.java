package com.studylib.modules.message.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.IdResponseVO;
import com.studylib.modules.message.dto.AdminMessageCreateRequestDTO;
import com.studylib.modules.message.service.AdminMessageService;

public class AdminMessageController {
    private final AdminMessageService adminMessageService;

    public AdminMessageController(AdminMessageService adminMessageService) {
        this.adminMessageService = adminMessageService;
    }

    public ApiResponse<IdResponseVO> create(AdminMessageCreateRequestDTO request) {
        return ApiResponse.success(adminMessageService.createMessage(request));
    }
}
