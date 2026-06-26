package com.studylib.modules.questionBank.service.impl;

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
import com.studylib.modules.questionBank.assembler.QuestionBankAssembler;
import com.studylib.modules.questionBank.dto.QuestionBankQueryDTO;
import com.studylib.modules.questionBank.dto.QuestionBankSaveRequestDTO;
import com.studylib.modules.questionBank.entity.QuestionBankEntity;
import com.studylib.modules.questionBank.mapper.QuestionBankMapper;
import com.studylib.modules.questionBank.service.AdminQuestionBankService;
import com.studylib.modules.questionBank.vo.QuestionBankVO;
import org.springframework.stereotype.Service;

@Service
public class AdminQuestionBankServiceImpl implements AdminQuestionBankService {

  private final QuestionBankMapper questionBankMapper;
  private final AuditLogRepository auditLogRepository;
  private final QuestionBankAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminQuestionBankServiceImpl(QuestionBankMapper questionBankMapper, AuditLogRepository auditLogRepository,
      QuestionBankAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.questionBankMapper = questionBankMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<QuestionBankVO> getQuestionBankList(QuestionBankQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = questionBankMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        questionBankMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public QuestionBankVO saveQuestionBank(QuestionBankSaveRequestDTO request) {
    Long bankId = request.getId() != null && request.getId() > 0 ? request.getId() : request.getBankId();
    boolean creating = bankId == null || bankId <= 0;
    QuestionBankEntity entity = creating ? new QuestionBankEntity() : questionBankMapper.selectById(bankId);
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Question bank not found");
    }

    entity.setId(bankId);
    entity.setBankName(request.getBankName().trim());
    entity.setCategoryName(request.getCategoryName().trim());
    entity.setQuestionCount(request.getQuestionCount());
    entity.setDifficulty(request.getDifficulty().trim());
    entity.setStatus(request.getStatus().trim());
    entity.setUpdateTime(DateTimeUtils.now());

    if (creating) {
      questionBankMapper.insert(entity);
    } else {
      questionBankMapper.update(entity);
    }

    QuestionBankVO saved = assembler.toVO(questionBankMapper.selectById(entity.getId()));
    auditLogRepository.appendOperationLog("题库管理", creating ? "新增" : "编辑", (creating ? "创建题库 " : "更新题库 ") + saved.bankName());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteQuestionBank(Long id) {
    QuestionBankEntity entity = questionBankMapper.selectById(id);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Question bank not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> questionBankMapper.deleteById(id, item.getUpdatedAt(), item.getUpdatedBy()),
        "题库管理",
        "删除题库 " + entity.getBankName()
    );
  }

}
