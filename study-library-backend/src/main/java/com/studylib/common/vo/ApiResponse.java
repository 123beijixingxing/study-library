package com.studylib.common.vo;

import com.studylib.common.constants.ErrorCodeConstants;
import java.util.UUID;

public record ApiResponse<T>(Integer code, String message, T data, String requestId, Long timestamp) {

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(
        ErrorCodeConstants.SUCCESS,
        "success",
        data,
        UUID.randomUUID().toString(),
        System.currentTimeMillis()
    );
  }

  public static <T> ApiResponse<T> failure(Integer code, String message) {
    return new ApiResponse<>(
        code,
        message,
        null,
        UUID.randomUUID().toString(),
        System.currentTimeMillis()
    );
  }
}
