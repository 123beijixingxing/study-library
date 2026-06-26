package com.studylib.modules.practice.vo;

import com.studylib.modules.question.vo.QuestionVO;
import java.util.List;

public record PracticePaperDetailVO(
    long paperId,
    String paperName,
    String paperType,
    String courseName,
    String ruleDesc,
    List<QuestionVO> questionList
) {
}
