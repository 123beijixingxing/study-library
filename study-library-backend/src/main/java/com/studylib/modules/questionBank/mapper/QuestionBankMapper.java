package com.studylib.modules.questionBank.mapper;

import com.studylib.modules.questionBank.entity.QuestionBankEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface QuestionBankMapper {

  QuestionBankEntity selectById(@Param("id") Long id);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<QuestionBankEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(QuestionBankEntity entity);

  Integer update(QuestionBankEntity entity);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
