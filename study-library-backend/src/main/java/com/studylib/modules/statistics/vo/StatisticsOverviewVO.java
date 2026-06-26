package com.studylib.modules.statistics.vo;

public record StatisticsOverviewVO(
    long totalUsers,
    long totalCourses,
    long totalQuestions,
    long totalInteractions
) {
}
