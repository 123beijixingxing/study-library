package com.studylib.infrastructure.persistence;

import com.studylib.modules.log.entity.LoginLogEntity;
import com.studylib.modules.log.entity.OperationLogEntity;
import com.studylib.common.vo.PageResponse;
import java.util.List;

public interface AuditLogRepository {

  List<OperationLogEntity> listOperationLogs();

  List<OperationLogEntity> listOperationLogs(String keyword, String operationType);

  PageResponse<OperationLogEntity> pageOperationLogs(String keyword, String operationType, Integer pageNum, Integer pageSize);

  List<LoginLogEntity> listLoginLogs();

  List<LoginLogEntity> listLoginLogs(String keyword, String status);

  PageResponse<LoginLogEntity> pageLoginLogs(String keyword, String status, Integer pageNum, Integer pageSize);

  OperationLogEntity appendOperationLog(String operatorName, String operationModule, String operationType, String operationContent, String ip);

  OperationLogEntity appendOperationLog(String operationModule, String operationType, String operationContent);

  LoginLogEntity appendLoginLog(String username, String status, String ip);

  LoginLogEntity appendLoginLog(String username, String status);
}
