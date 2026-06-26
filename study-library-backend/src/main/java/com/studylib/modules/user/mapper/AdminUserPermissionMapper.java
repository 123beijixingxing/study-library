package com.studylib.modules.user.mapper;

import com.studylib.modules.user.entity.UserPermissionEntity;
import org.apache.ibatis.annotations.Param;

public interface AdminUserPermissionMapper {

  UserPermissionEntity selectByUserId(@Param("userId") Long userId);

  Integer insert(UserPermissionEntity entity);

  Integer update(UserPermissionEntity entity);

  Integer deleteByUserId(@Param("userId") Long userId);
}
