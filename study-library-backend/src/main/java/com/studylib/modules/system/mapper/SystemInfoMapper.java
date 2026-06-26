package com.studylib.modules.system.mapper;

import com.studylib.modules.system.entity.SystemInfoEntity;

public interface SystemInfoMapper {

  SystemInfoEntity selectDefault();

  Integer updateDefault(SystemInfoEntity entity);
}
