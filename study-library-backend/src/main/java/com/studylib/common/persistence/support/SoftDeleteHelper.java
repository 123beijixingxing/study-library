package com.studylib.common.persistence.support;

import com.studylib.common.persistence.model.BaseAuditEntity;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class SoftDeleteHelper {

  private final AuditFieldHelper auditFieldHelper;
  private final AuditLogRepository auditLogRepository;

  public SoftDeleteHelper(AuditFieldHelper auditFieldHelper, AuditLogRepository auditLogRepository) {
    this.auditFieldHelper = auditFieldHelper;
    this.auditLogRepository = auditLogRepository;
  }

  public <T extends BaseAuditEntity> SuccessResponseVO softDelete(T entity, Consumer<T> deleteAction, String moduleName, String operationContent) {
    auditFieldHelper.fillForSoftDelete(entity);
    deleteAction.accept(entity);
    auditLogRepository.appendOperationLog(moduleName, "删除", operationContent);
    return SuccessResponseVO.ok();
  }
}
