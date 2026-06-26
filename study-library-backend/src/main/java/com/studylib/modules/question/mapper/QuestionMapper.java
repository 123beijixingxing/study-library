package com.studylib.modules.question.mapper;

import com.studylib.modules.question.entity.QuestionEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionMapper {

  Long countAll();

  Long countByBankId(@Param("bankId") Long bankId);

  QuestionEntity selectByQuestionId(@Param("questionId") Long questionId);

  List<QuestionEntity> selectByQuery(@Param("bankId") Long bankId, @Param("keyword") String keyword, @Param("questionType") String questionType,
      @Param("status") String status, @Param("templateCode") String templateCode, @Param("versionGroupId") String versionGroupId);

  List<QuestionEntity> selectByVersionGroupId(@Param("versionGroupId") String versionGroupId);

  List<QuestionEntity> selectLatestByBankId(@Param("bankId") Long bankId, @Param("limit") Integer limit);

  Integer insert(QuestionEntity entity);

  Integer update(QuestionEntity entity);

  Long selectMaxVersionNoByVersionGroupId(@Param("versionGroupId") String versionGroupId);

  Integer deleteByQuestionId(@Param("questionId") Long questionId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
