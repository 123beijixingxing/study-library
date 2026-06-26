package com.studylib.modules.service.mapper;

import com.studylib.modules.service.entity.ServiceTicketEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ServiceTicketMapper {

  ServiceTicketEntity selectById(@Param("id") Long id);

  Long countPage(@Param("keyword") String keyword, @Param("status") String status);

  List<ServiceTicketEntity> selectPage(@Param("keyword") String keyword, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer update(ServiceTicketEntity entity);

  Integer deleteById(@Param("id") Long id, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
