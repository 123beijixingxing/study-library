package com.studylib.modules.risk.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.vo.PageResponse;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.risk.assembler.ReportAssembler;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;
import com.studylib.modules.risk.dto.ReportQueryDTO;
import com.studylib.modules.risk.entity.ReportEntity;
import com.studylib.modules.risk.mapper.ReportMapper;
import com.studylib.modules.risk.service.AdminReportService;
import com.studylib.modules.risk.vo.ReportVO;
import org.springframework.stereotype.Service;

@Service
public class AdminReportServiceImpl implements AdminReportService {

  private final ReportMapper reportMapper;
  private final AuditLogRepository auditLogRepository;
  private final ReportAssembler assembler;

  public AdminReportServiceImpl(ReportMapper reportMapper, AuditLogRepository auditLogRepository, ReportAssembler assembler) {
    this.reportMapper = reportMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
  }

  @Override
  public PageResponse<ReportVO> getReportList(ReportQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = reportMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getReportType()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        reportMapper.selectPage(
                normalizeKeyword(query.getKeyword()),
                normalizeFilter(query.getReportType()),
                normalizeFilter(query.getStatus()),
                offset(currentPageNum, currentPageSize),
                currentPageSize
            )
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public ReportVO handleReport(Long reportId, ReportHandleRequestDTO request) {
    ReportEntity entity = reportMapper.selectById(reportId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Report not found");
    }
    if (request.getStatus() != null && !request.getStatus().isBlank()) {
      entity.setStatus(request.getStatus().trim());
    }
    if (request.getHandleResult() != null && !request.getHandleResult().isBlank()) {
      entity.setHandleResult(request.getHandleResult().trim());
    }
    if (request.getHandleRemark() != null && !request.getHandleRemark().isBlank()) {
      entity.setHandleRemark(request.getHandleRemark().trim());
    }
    reportMapper.update(entity);
    ReportVO saved = assembler.toVO(reportMapper.selectById(reportId));
    auditLogRepository.appendOperationLog("举报审核", "编辑", "处理举报 " + reportId);
    return saved;
  }

}
