package com.studylib.modules.practice.mapper;

import com.studylib.modules.practice.entity.PracticeWrongQuestionStatEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PracticeWrongQuestionStatMapper {

  Long countByPaperId(@Param("paperId") Long paperId);

  List<PracticeWrongQuestionStatEntity> selectByPaperId(@Param("paperId") Long paperId);

  Integer softDeleteByPaperId(@Param("paperId") Long paperId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);

  Integer insertBatch(@Param("paperId") Long paperId, @Param("statList") List<PracticeWrongQuestionStatEntity> statList);
}
