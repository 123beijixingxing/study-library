package com.studylib.modules.practice.vo;

public record PracticeTrendPointVO(
    String date,
    int submitCount,
    int avgScore
) {
}
