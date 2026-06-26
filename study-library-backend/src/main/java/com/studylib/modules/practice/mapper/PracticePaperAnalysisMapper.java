package com.studylib.modules.practice.mapper;

import com.studylib.modules.practice.entity.PracticePaperAnalysisEntity;

public interface PracticePaperAnalysisMapper {

  PracticePaperAnalysisEntity selectByPaperId(Long paperId);

  Integer insert(PracticePaperAnalysisEntity entity);

  Integer update(PracticePaperAnalysisEntity entity);
}
