package com.studylib.modules.course.vo;

import java.util.List;

public record AdminCourseDetailVO(Long courseId, String teacherName, String summary, List<AdminCourseChapterVO> chapterList) {
}
