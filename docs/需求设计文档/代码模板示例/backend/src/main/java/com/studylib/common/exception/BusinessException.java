package com.studylib.common.exception;

public class BusinessException extends RuntimeException {
    public final int code;
    public final int httpStatus;

    public BusinessException(int code, String message, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
