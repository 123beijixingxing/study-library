package com.studylib.modules.practice.mapper;

import com.studylib.modules.practice.entity.PracticePaperQuestionEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PracticePaperQuestionMapper {

  Long countByPaperId(@Param("paperId") Long paperId);

  List<PracticePaperQuestionEntity> selectByPaperId(@Param("paperId") Long paperId);

  Integer softDeleteByPaperId(@Param("paperId") Long paperId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);

  Integer insertBatch(@Param("paperId") Long paperId, @Param("questionList") List<PracticePaperQuestionEntity> questionList);
}
