package com.studylib.modules.system.controller;

import com.studylib.common.vo.ApiResponse;
import com.studylib.modules.system.service.SystemConfigService;

public class SystemConfigController {
    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    public ApiResponse<Object> getSystemInfo() {
        return ApiResponse.success(systemConfigService.getSystemInfo());
    }

    public ApiResponse<Object> getFeatureConfig() {
        return ApiResponse.success(systemConfigService.getFeatureConfig());
    }
}
