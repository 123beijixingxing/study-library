package com.studylib.modules.home.mapper;

import com.studylib.modules.home.entity.HomeSectionEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HomeSectionMapper {

  HomeSectionEntity selectById(@Param("id") Long id);

  Long countPage(@Param("sectionType") String sectionType);

  List<HomeSectionEntity> selectPage(@Param("sectionType") String sectionType, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer update(HomeSectionEntity entity);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
