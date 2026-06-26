package com.studylib.modules.practice.mapper;

import java.util.List;

public interface PracticeMapper {
    Object selectPublishedPaperById(Long paperId);

    List<Object> selectQuestionsByPaperId(Long paperId);

    int insertPracticeRecord(Object recordEntity);

    int insertPracticeAnswers(List<Object> answerEntities);
}
