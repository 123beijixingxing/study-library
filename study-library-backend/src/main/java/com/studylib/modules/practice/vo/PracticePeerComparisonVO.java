package com.studylib.modules.practice.vo;

public record PracticePeerComparisonVO(
    long paperId,
    String paperName,
    String courseName,
    int avgScore,
    int passRate,
    int totalSubmitCount,
    String status
) {
}
