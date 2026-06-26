package com.studylib.modules.log.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.map;

import com.studylib.common.vo.PageResponse;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.log.assembler.AdminLogAssembler;
import com.studylib.modules.log.dto.LoginLogQueryDTO;
import com.studylib.modules.log.dto.OperationLogQueryDTO;
import com.studylib.modules.log.service.AdminLogService;
import com.studylib.modules.log.vo.LoginLogVO;
import com.studylib.modules.log.vo.OperationLogVO;
import org.springframework.stereotype.Service;

@Service
public class AdminLogServiceImpl implements AdminLogService {

  private final AuditLogRepository repository;
  private final AdminLogAssembler assembler;

  public AdminLogServiceImpl(AuditLogRepository repository, AdminLogAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @Override
  public PageResponse<OperationLogVO> getOperationLogs(OperationLogQueryDTO query) {
    return map(repository.pageOperationLogs(
        query.getKeyword(),
        query.getOperationType(),
        query.safePageNum(),
        query.safePageSize()
    ), assembler::toOperationVO);
  }

  @Override
  public PageResponse<LoginLogVO> getLoginLogs(LoginLogQueryDTO query) {
    return map(repository.pageLoginLogs(
        query.getKeyword(),
        query.getStatus(),
        query.safePageNum(),
        query.safePageSize()
    ), assembler::toLoginVO);
  }
}
