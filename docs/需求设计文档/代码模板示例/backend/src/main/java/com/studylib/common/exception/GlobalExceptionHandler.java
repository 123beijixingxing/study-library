package com.studylib.common.exception;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.vo.ApiResponse;

public class GlobalExceptionHandler {
    public ApiResponse<Object> handleBusinessException(BusinessException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.code = ex.code;
        response.message = ex.getMessage();
        response.data = null;
        response.timestamp = System.currentTimeMillis();
        return response;
    }

    public ApiResponse<Object> handleUnknownException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.code = ErrorCodeConstants.SYSTEM_ERROR;
        response.message = "系统异常，请稍后重试";
        response.data = null;
        response.timestamp = System.currentTimeMillis();
        return response;
    }
}
