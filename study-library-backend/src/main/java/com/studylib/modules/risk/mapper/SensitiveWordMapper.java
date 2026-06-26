package com.studylib.modules.risk.mapper;

import com.studylib.modules.risk.entity.SensitiveWordEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SensitiveWordMapper {

  SensitiveWordEntity selectById(@Param("wordId") Long wordId);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<SensitiveWordEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(SensitiveWordEntity entity);

  Integer update(SensitiveWordEntity entity);

  Integer deleteById(@Param("wordId") Long wordId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
