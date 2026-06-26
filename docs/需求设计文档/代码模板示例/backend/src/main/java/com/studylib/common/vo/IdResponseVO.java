package com.studylib.common.vo;

public class IdResponseVO {
    public Long id;

    public static IdResponseVO of(Long id) {
        IdResponseVO response = new IdResponseVO();
        response.id = id;
        return response;
    }
}
