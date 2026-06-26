package com.studylib.modules.member.mapper;

import com.studylib.modules.member.entity.StudyRecordEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudyRecordMapper {

  StudyRecordEntity selectById(@Param("recordId") Long recordId);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<StudyRecordEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer deleteById(@Param("recordId") Long recordId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
