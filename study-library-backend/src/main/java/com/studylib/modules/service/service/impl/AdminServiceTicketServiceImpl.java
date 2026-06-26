package com.studylib.modules.service.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.service.assembler.ServiceTicketAssembler;
import com.studylib.modules.service.dto.ServiceTicketQueryDTO;
import com.studylib.modules.service.dto.ServiceTicketReplyRequestDTO;
import com.studylib.modules.service.entity.ServiceTicketEntity;
import com.studylib.modules.service.mapper.ServiceTicketMapper;
import com.studylib.modules.service.service.AdminServiceTicketService;
import com.studylib.modules.service.vo.ServiceTicketVO;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceTicketServiceImpl implements AdminServiceTicketService {

  private final ServiceTicketMapper serviceTicketMapper;
  private final AuditLogRepository auditLogRepository;
  private final ServiceTicketAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminServiceTicketServiceImpl(ServiceTicketMapper serviceTicketMapper, AuditLogRepository auditLogRepository,
      ServiceTicketAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.serviceTicketMapper = serviceTicketMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<ServiceTicketVO> getTicketList(ServiceTicketQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = serviceTicketMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        serviceTicketMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public ServiceTicketVO replyTicket(ServiceTicketReplyRequestDTO request) {
    ServiceTicketEntity entity = serviceTicketMapper.selectById(request.getTicketId());
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Service ticket not found");
    }

    if (request.getReplyContent() != null && !request.getReplyContent().isBlank()) {
      entity.setLatestMessage(request.getReplyContent().trim());
    }
    if (request.getStatus() != null && !request.getStatus().isBlank()) {
      entity.setStatus(request.getStatus().trim());
    } else if (request.getReplyContent() != null && !request.getReplyContent().isBlank()) {
      entity.setStatus("processing");
    }
    entity.setUpdateTime(DateTimeUtils.now());
    serviceTicketMapper.update(entity);

    ServiceTicketVO saved = assembler.toVO(serviceTicketMapper.selectById(request.getTicketId()));
    auditLogRepository.appendOperationLog("客服管理", "编辑", "处理工单 " + saved.id());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteTicket(Long id) {
    ServiceTicketEntity entity = serviceTicketMapper.selectById(id);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Service ticket not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> serviceTicketMapper.deleteById(id, item.getUpdatedAt(), item.getUpdatedBy()),
        "客服管理",
        "删除工单 " + entity.getId()
    );
  }

}
