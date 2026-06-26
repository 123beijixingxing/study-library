package com.studylib.modules.practice.mapper;

import com.studylib.modules.practice.entity.PracticePaperEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PracticePaperMapper {

  PracticePaperEntity selectById(@Param("paperId") Long paperId);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<PracticePaperEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(PracticePaperEntity entity);

  Integer update(PracticePaperEntity entity);

  Integer deleteById(@Param("paperId") Long paperId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
