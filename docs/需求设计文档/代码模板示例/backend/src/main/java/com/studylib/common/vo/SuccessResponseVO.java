package com.studylib.common.vo;

public class SuccessResponseVO {
    public Boolean success;

    public static SuccessResponseVO ok() {
        SuccessResponseVO response = new SuccessResponseVO();
        response.success = true;
        return response;
    }
}
