package com.studylib.modules.message.mapper;

import com.studylib.modules.message.entity.AdminMessageEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminMessageMapper {

  AdminMessageEntity selectById(@Param("id") Long id);

  Long countPage(@Param("keyword") String keyword, @Param("sendStatus") String sendStatus);

  List<AdminMessageEntity> selectPage(@Param("keyword") String keyword, @Param("sendStatus") String sendStatus,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(AdminMessageEntity entity);

  Integer update(AdminMessageEntity entity);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
