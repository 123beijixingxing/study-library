package com.studylib.modules.user.mapper;

import com.studylib.modules.user.entity.AdminUserEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {

  AdminUserEntity selectById(@Param("id") Long id);

  AdminUserEntity selectByUsername(@Param("username") String username);

  Long countPage(@Param("keyword") String keyword, @Param("role") String role, @Param("status") String status);

  List<AdminUserEntity> selectPage(@Param("keyword") String keyword, @Param("role") String role, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(AdminUserEntity entity);

  Integer update(AdminUserEntity entity);

  Integer updateStatus(@Param("id") Long id, @Param("status") String status, @Param("updatedAt") LocalDateTime updatedAt,
      @Param("updatedBy") String updatedBy);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
