package com.studylib.modules.risk.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.common.util.DateTimeUtils;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.risk.assembler.SensitiveWordAssembler;
import com.studylib.modules.risk.dto.SensitiveWordQueryDTO;
import com.studylib.modules.risk.dto.SensitiveWordSaveRequestDTO;
import com.studylib.modules.risk.entity.SensitiveWordEntity;
import com.studylib.modules.risk.mapper.SensitiveWordMapper;
import com.studylib.modules.risk.service.AdminSensitiveWordService;
import com.studylib.modules.risk.vo.SensitiveWordVO;
import org.springframework.stereotype.Service;

@Service
public class AdminSensitiveWordServiceImpl implements AdminSensitiveWordService {

  private final SensitiveWordMapper sensitiveWordMapper;
  private final AuditLogRepository auditLogRepository;
  private final SensitiveWordAssembler assembler;
  private final AuditFieldHelper auditFieldHelper;

  public AdminSensitiveWordServiceImpl(SensitiveWordMapper sensitiveWordMapper, AuditLogRepository auditLogRepository,
      SensitiveWordAssembler assembler, AuditFieldHelper auditFieldHelper) {
    this.sensitiveWordMapper = sensitiveWordMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.auditFieldHelper = auditFieldHelper;
  }

  @Override
  public PageResponse<SensitiveWordVO> getWordList(SensitiveWordQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = sensitiveWordMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        sensitiveWordMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public SensitiveWordVO saveWord(SensitiveWordSaveRequestDTO request) {
    boolean creating = request.getWordId() == null || request.getWordId() <= 0;
    SensitiveWordEntity entity = creating ? new SensitiveWordEntity() : sensitiveWordMapper.selectById(request.getWordId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Sensitive word not found");
    }

    entity.setWordId(request.getWordId());
    entity.setWord(request.getWord().trim());
    entity.setReplaceText(request.getReplaceText().trim());
    entity.setStatus(request.getStatus().trim());
    entity.setUpdateTime(DateTimeUtils.now());

    if (creating) {
      sensitiveWordMapper.insert(entity);
    } else {
      sensitiveWordMapper.update(entity);
    }

    SensitiveWordVO saved = assembler.toVO(sensitiveWordMapper.selectById(entity.getWordId()));
    auditLogRepository.appendOperationLog("敏感词管理", creating ? "新增" : "编辑", (creating ? "创建敏感词 " : "更新敏感词 ") + saved.word());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteWord(Long wordId) {
    SensitiveWordEntity entity = sensitiveWordMapper.selectById(wordId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Sensitive word not found");
    }
    auditFieldHelper.fillForSoftDelete(entity);
    sensitiveWordMapper.deleteById(wordId, entity.getUpdatedAt(), entity.getUpdatedBy());
    auditLogRepository.appendOperationLog("敏感词管理", "删除", "删除敏感词 " + entity.getWord());
    return SuccessResponseVO.ok();
  }

}
