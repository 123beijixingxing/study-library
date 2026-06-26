package com.studylib.modules.garden.mapper;

import com.studylib.modules.garden.entity.GardenContentEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GardenContentMapper {

  GardenContentEntity selectById(@Param("id") Long id);

  Long countPage(@Param("contentType") String contentType, @Param("auditStatus") String auditStatus);

  List<GardenContentEntity> selectPage(@Param("contentType") String contentType, @Param("auditStatus") String auditStatus,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer update(GardenContentEntity entity);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
