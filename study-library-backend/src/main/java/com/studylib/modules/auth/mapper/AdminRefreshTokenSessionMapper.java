package com.studylib.modules.auth.mapper;

import com.studylib.modules.auth.entity.AdminRefreshTokenSessionEntity;
import org.apache.ibatis.annotations.Param;

public interface AdminRefreshTokenSessionMapper {

  AdminRefreshTokenSessionEntity selectBySessionId(@Param("sessionId") String sessionId);

  Integer insert(AdminRefreshTokenSessionEntity entity);

  Integer update(AdminRefreshTokenSessionEntity entity);

  Integer revokeBySessionId(@Param("sessionId") String sessionId, @Param("updatedAt") Long updatedAt);

  Integer deleteByUserId(@Param("userId") Long userId);
}
