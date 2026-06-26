package com.studylib.infrastructure.persistence;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;

import com.studylib.common.security.AdminRequestContext;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.modules.log.entity.LoginLogEntity;
import com.studylib.modules.log.entity.OperationLogEntity;
import com.studylib.modules.log.mapper.LoginLogMapper;
import com.studylib.modules.log.mapper.OperationLogMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseAuditLogRepository implements AuditLogRepository {

  private final OperationLogMapper operationLogMapper;
  private final LoginLogMapper loginLogMapper;

  public DatabaseAuditLogRepository(OperationLogMapper operationLogMapper, LoginLogMapper loginLogMapper) {
    this.operationLogMapper = operationLogMapper;
    this.loginLogMapper = loginLogMapper;
  }

  @Override
  public List<OperationLogEntity> listOperationLogs() {
    return listOperationLogs(null, null);
  }

  @Override
  public List<OperationLogEntity> listOperationLogs(String keyword, String operationType) {
    return operationLogMapper.selectPage(normalizeKeyword(keyword), normalizeFilter(operationType), 0, Integer.MAX_VALUE);
  }

  @Override
  public PageResponse<OperationLogEntity> pageOperationLogs(String keyword, String operationType, Integer pageNum, Integer pageSize) {
    int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
    int safePageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
    int currentOffset = offset(safePageNum, safePageSize);
    Long total = operationLogMapper.countPage(normalizeKeyword(keyword), normalizeFilter(operationType));
    List<OperationLogEntity> list = operationLogMapper.selectPage(normalizeKeyword(keyword), normalizeFilter(operationType), currentOffset, safePageSize);
    return build(safePageNum, safePageSize, total, list);
  }

  @Override
  public List<LoginLogEntity> listLoginLogs() {
    return listLoginLogs(null, null);
  }

  @Override
  public List<LoginLogEntity> listLoginLogs(String keyword, String status) {
    return loginLogMapper.selectPage(normalizeKeyword(keyword), normalizeFilter(status), 0, Integer.MAX_VALUE);
  }

  @Override
  public PageResponse<LoginLogEntity> pageLoginLogs(String keyword, String status, Integer pageNum, Integer pageSize) {
    int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
    int safePageSize = pageSize == null || pageSize < 1 ? 10 : pageSize;
    int currentOffset = offset(safePageNum, safePageSize);
    Long total = loginLogMapper.countPage(normalizeKeyword(keyword), normalizeFilter(status));
    List<LoginLogEntity> list = loginLogMapper.selectPage(normalizeKeyword(keyword), normalizeFilter(status), currentOffset, safePageSize);
    return build(safePageNum, safePageSize, total, list);
  }

  @Override
  public OperationLogEntity appendOperationLog(String operatorName, String operationModule, String operationType, String operationContent, String ip) {
    OperationLogEntity entity = new OperationLogEntity();
    entity.setOperateTime(DateTimeUtils.now());
    entity.setOperatorName(operatorName);
    entity.setOperationModule(operationModule);
    entity.setOperationType(operationType);
    entity.setOperationContent(operationContent);
    entity.setIp(ip);
    operationLogMapper.insert(entity);
    return entity;
  }

  @Override
  public OperationLogEntity appendOperationLog(String operationModule, String operationType, String operationContent) {
    return appendOperationLog(
        AdminRequestContext.getCurrentUsername(),
        operationModule,
        operationType,
        operationContent,
        AdminRequestContext.getCurrentRequestIp()
    );
  }

  @Override
  public LoginLogEntity appendLoginLog(String username, String status, String ip) {
    LoginLogEntity entity = new LoginLogEntity();
    entity.setLoginTime(DateTimeUtils.now());
    entity.setUsername(username);
    entity.setLoginType("密码登录");
    entity.setStatus(status);
    entity.setIp(ip);
    loginLogMapper.insert(entity);
    return entity;
  }

  @Override
  public LoginLogEntity appendLoginLog(String username, String status) {
    return appendLoginLog(username, status, AdminRequestContext.getCurrentRequestIp());
  }

}
