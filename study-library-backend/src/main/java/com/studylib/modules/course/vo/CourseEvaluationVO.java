package com.studylib.modules.course.vo;

import java.time.LocalDateTime;

public record CourseEvaluationVO(Long evaluationId, String courseName, String username, Integer score, String content, String status, LocalDateTime createTime) {
}
