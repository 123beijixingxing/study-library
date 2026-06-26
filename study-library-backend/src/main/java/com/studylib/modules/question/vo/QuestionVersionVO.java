package com.studylib.modules.question.vo;

public record QuestionVersionVO(
    long questionId,
    String versionGroupId,
    int versionNo,
    String versionRemark,
    String sourceAction,
    Long sourceQuestionId,
    Integer sourceVersionNo,
    String title,
    String status,
    String updateTime
) {
}
