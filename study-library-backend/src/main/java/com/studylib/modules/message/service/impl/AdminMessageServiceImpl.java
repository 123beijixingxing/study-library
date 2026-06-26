package com.studylib.modules.message.service.impl;

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
import com.studylib.modules.message.assembler.AdminMessageAssembler;
import com.studylib.modules.message.dto.AdminMessageQueryDTO;
import com.studylib.modules.message.dto.AdminMessageSaveRequestDTO;
import com.studylib.modules.message.entity.AdminMessageEntity;
import com.studylib.modules.message.mapper.AdminMessageMapper;
import com.studylib.modules.message.service.AdminMessageService;
import com.studylib.modules.message.vo.AdminMessageVO;
import org.springframework.stereotype.Service;

@Service
public class AdminMessageServiceImpl implements AdminMessageService {

  private final AdminMessageMapper adminMessageMapper;
  private final AuditLogRepository auditLogRepository;
  private final AdminMessageAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminMessageServiceImpl(AdminMessageMapper adminMessageMapper, AuditLogRepository auditLogRepository, AdminMessageAssembler assembler,
      SoftDeleteHelper softDeleteHelper) {
    this.adminMessageMapper = adminMessageMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<AdminMessageVO> getMessageList(AdminMessageQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = adminMessageMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getSendStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        adminMessageMapper.selectPage(
                normalizeKeyword(query.getKeyword()),
                normalizeFilter(query.getSendStatus()),
                offset(currentPageNum, currentPageSize),
                currentPageSize
            )
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public AdminMessageVO saveMessage(AdminMessageSaveRequestDTO request) {
    boolean creating = request.getId() == null;
    AdminMessageEntity entity = creating ? new AdminMessageEntity() : adminMessageMapper.selectById(request.getId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Message not found");
    }

    entity.setId(request.getId());
    entity.setTitle(request.getTitle().trim());
    entity.setMessageType(request.getMessageType().trim());
    entity.setReceiverType(request.getReceiverType().trim());
    entity.setSendStatus(request.getSendStatus().trim());
    if ("sent".equalsIgnoreCase(entity.getSendStatus())) {
      entity.setSendTime(DateTimeUtils.now());
    } else {
      entity.setSendTime(DateTimeUtils.parseDateTimeOrNow(request.getSendTime()));
    }

    if (creating) {
      adminMessageMapper.insert(entity);
    } else {
      adminMessageMapper.update(entity);
    }

    AdminMessageVO saved = assembler.toVO(adminMessageMapper.selectById(entity.getId()));
    auditLogRepository.appendOperationLog("消息管理", creating ? "新增" : "编辑", (creating ? "创建消息 " : "更新消息 ") + saved.title());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteMessage(Long id) {
    AdminMessageEntity entity = adminMessageMapper.selectById(id);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Message not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> adminMessageMapper.deleteById(id, item.getUpdatedAt(), item.getUpdatedBy()),
        "消息管理",
        "删除消息 " + entity.getTitle()
    );
  }

}
