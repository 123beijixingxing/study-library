package com.studylib.modules.question.vo;

import java.util.List;

public record QuestionVO(
    long questionId,
    long bankId,
    String chapterName,
    String templateCode,
    String versionGroupId,
    int versionNo,
    String versionRemark,
    String sourceAction,
    Long sourceQuestionId,
    Integer sourceVersionNo,
    String title,
    String questionType,
    String difficulty,
    String status,
    String updateTime,
    List<QuestionOptionVO> optionList,
    String answerText,
    String analysisText
) {
}
