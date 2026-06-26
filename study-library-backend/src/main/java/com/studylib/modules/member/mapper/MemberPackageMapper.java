package com.studylib.modules.member.mapper;

import com.studylib.modules.member.entity.MemberPackageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MemberPackageMapper {

  MemberPackageEntity selectById(@Param("packageId") Long packageId);

  Long countPage(@Param("status") String status);

  List<MemberPackageEntity> selectPage(@Param("status") String status, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(MemberPackageEntity entity);

  Integer update(MemberPackageEntity entity);

  Integer deleteById(@Param("packageId") Long packageId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
