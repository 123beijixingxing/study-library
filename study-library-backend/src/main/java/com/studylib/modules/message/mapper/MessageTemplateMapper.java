package com.studylib.modules.message.mapper;

import com.studylib.modules.message.entity.MessageTemplateEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessageTemplateMapper {

  MessageTemplateEntity selectById(@Param("templateId") Long templateId);

  Long countPage(@Param("messageType") String messageType, @Param("status") String status);

  List<MessageTemplateEntity> selectPage(@Param("messageType") String messageType, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer insert(MessageTemplateEntity entity);

  Integer update(MessageTemplateEntity entity);

  Integer deleteById(@Param("templateId") Long templateId, @Param("updatedAt") LocalDateTime updatedAt, @Param("updatedBy") String updatedBy);
}
