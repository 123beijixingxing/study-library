package com.studylib.common.persistence.support;

import com.studylib.common.persistence.model.BaseAuditEntity;
import com.studylib.common.security.AdminRequestContext;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class AuditFieldHelper {

  public <T extends BaseAuditEntity> T fillForCreate(T entity) {
    LocalDateTime now = LocalDateTime.now();
    String username = AdminRequestContext.getCurrentUsername();
    entity.setCreatedAt(now);
    entity.setUpdatedAt(now);
    entity.setCreatedBy(username);
    entity.setUpdatedBy(username);
    entity.setDeleted(Boolean.FALSE);
    return entity;
  }

  public <T extends BaseAuditEntity> T fillForUpdate(T entity) {
    entity.setUpdatedAt(LocalDateTime.now());
    entity.setUpdatedBy(AdminRequestContext.getCurrentUsername());
    if (entity.getDeleted() == null) {
      entity.setDeleted(Boolean.FALSE);
    }
    return entity;
  }

  public <T extends BaseAuditEntity> T fillForSoftDelete(T entity) {
    fillForUpdate(entity);
    entity.setDeleted(Boolean.TRUE);
    return entity;
  }
}
