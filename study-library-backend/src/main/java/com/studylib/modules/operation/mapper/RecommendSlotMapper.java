package com.studylib.modules.operation.mapper;

import com.studylib.modules.operation.entity.RecommendSlotEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RecommendSlotMapper {

  RecommendSlotEntity selectById(@Param("slotId") Long slotId);

  Long countPage(@Param("pageCode") String pageCode, @Param("status") String status);

  List<RecommendSlotEntity> selectPage(@Param("pageCode") String pageCode, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(RecommendSlotEntity entity);

  Integer update(RecommendSlotEntity entity);

  Integer deleteById(@Param("slotId") Long slotId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
