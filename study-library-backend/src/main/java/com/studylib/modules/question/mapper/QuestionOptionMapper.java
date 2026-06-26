package com.studylib.modules.question.mapper;

import com.studylib.modules.question.entity.QuestionOptionEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionOptionMapper {

  List<QuestionOptionEntity> selectByQuestionIds(@Param("questionIds") List<Long> questionIds);

  Integer deleteByQuestionId(@Param("questionId") Long questionId);

  Integer insertBatch(@Param("questionId") Long questionId, @Param("optionList") List<QuestionOptionEntity> optionList);
}
