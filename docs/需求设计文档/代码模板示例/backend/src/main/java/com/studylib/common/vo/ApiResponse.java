package com.studylib.common.vo;

public class ApiResponse<T> {
    public Integer code;
    public String message;
    public T data;
    public String requestId;
    public Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 0;
        response.message = "success";
        response.data = data;
        response.timestamp = System.currentTimeMillis();
        return response;
    }
}
