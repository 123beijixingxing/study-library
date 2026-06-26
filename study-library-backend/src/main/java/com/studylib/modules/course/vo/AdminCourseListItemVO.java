package com.studylib.modules.course.vo;

import java.time.LocalDateTime;

public record AdminCourseListItemVO(
    Long id,
    String courseName,
    String categoryName,
    Integer hotScore,
    Integer chapterCount,
    String status,
    LocalDateTime updateTime
) {
}
