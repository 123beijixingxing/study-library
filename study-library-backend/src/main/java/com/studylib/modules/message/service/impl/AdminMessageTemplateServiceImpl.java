package com.studylib.modules.message.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
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
import com.studylib.modules.message.assembler.MessageTemplateAssembler;
import com.studylib.modules.message.dto.MessageTemplateQueryDTO;
import com.studylib.modules.message.dto.MessageTemplateSaveRequestDTO;
import com.studylib.modules.message.entity.MessageTemplateEntity;
import com.studylib.modules.message.mapper.MessageTemplateMapper;
import com.studylib.modules.message.service.AdminMessageTemplateService;
import com.studylib.modules.message.vo.MessageTemplateVO;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class AdminMessageTemplateServiceImpl implements AdminMessageTemplateService {

  private final MessageTemplateMapper messageTemplateMapper;
  private final AuditLogRepository auditLogRepository;
  private final MessageTemplateAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminMessageTemplateServiceImpl(MessageTemplateMapper messageTemplateMapper, AuditLogRepository auditLogRepository,
      MessageTemplateAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.messageTemplateMapper = messageTemplateMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<MessageTemplateVO> getTemplateList(MessageTemplateQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = messageTemplateMapper.countPage(normalizeFilter(query.getMessageType()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        messageTemplateMapper.selectPage(normalizeFilter(query.getMessageType()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public MessageTemplateVO saveTemplate(MessageTemplateSaveRequestDTO request) {
    boolean creating = request.getTemplateId() == null || request.getTemplateId() <= 0;
    MessageTemplateEntity entity = creating ? new MessageTemplateEntity() : messageTemplateMapper.selectById(request.getTemplateId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Message template not found");
    }

    entity.setTemplateId(request.getTemplateId());
    entity.setTemplateName(request.getTemplateName().trim());
    entity.setMessageType(request.getMessageType().trim());
    entity.setTitleTemplate(request.getTitleTemplate().trim());
    entity.setContentTemplate(request.getContentTemplate().trim());
    entity.setChannelList(new ArrayList<>(request.getChannelList()));
    entity.setStatus(request.getStatus().trim());
    entity.setUpdateTime(DateTimeUtils.now());

    if (creating) {
      messageTemplateMapper.insert(entity);
    } else {
      messageTemplateMapper.update(entity);
    }

    MessageTemplateVO saved = assembler.toVO(messageTemplateMapper.selectById(entity.getTemplateId()));
    auditLogRepository.appendOperationLog("消息模板", creating ? "新增" : "编辑", (creating ? "创建模板 " : "更新模板 ") + saved.templateName());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteTemplate(Long templateId) {
    MessageTemplateEntity entity = messageTemplateMapper.selectById(templateId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Message template not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> messageTemplateMapper.deleteById(templateId, item.getUpdatedAt(), item.getUpdatedBy()),
        "消息模板",
        "删除模板 " + entity.getTemplateName()
    );
  }

}
