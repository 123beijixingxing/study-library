package com.studylib.modules.practice.vo;

public record WrongQuestionVO(
    long questionId,
    String questionTitle,
    int wrongCount,
    int wrongRate,
    String difficulty
) {
}
