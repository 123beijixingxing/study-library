package com.studylib.modules.question.vo;

public record QuestionOptionVO(
    String key,
    String label,
    boolean isCorrect
) {
}
