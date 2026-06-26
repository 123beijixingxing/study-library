package com.studylib.modules.course.assembler;

import com.studylib.modules.course.vo.CourseDetailResponseVO;

import java.util.ArrayList;

public final class CourseAssembler {
    private CourseAssembler() {
    }

    public static CourseDetailResponseVO toDetailResponse(Object courseEntity) {
        CourseDetailResponseVO response = new CourseDetailResponseVO();
        response.tagList = new ArrayList<>();
        response.chapterList = new ArrayList<>();
        return response;
    }
}
