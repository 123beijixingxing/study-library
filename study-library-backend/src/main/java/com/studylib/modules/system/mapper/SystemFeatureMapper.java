package com.studylib.modules.system.mapper;

import com.studylib.modules.system.entity.SystemFeatureEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SystemFeatureMapper {

  SystemFeatureEntity selectByCode(@Param("code") String code);

  List<SystemFeatureEntity> selectList();

  Integer update(SystemFeatureEntity entity);
}
