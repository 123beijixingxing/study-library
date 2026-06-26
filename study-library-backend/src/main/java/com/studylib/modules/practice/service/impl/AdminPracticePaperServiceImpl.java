package com.studylib.modules.practice.service.impl;

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
import com.studylib.modules.practice.assembler.PracticePaperAssembler;
import com.studylib.modules.practice.dto.PracticePaperDetailSaveRequestDTO;
import com.studylib.modules.practice.dto.PracticePaperQueryDTO;
import com.studylib.modules.practice.dto.PracticePaperSaveRequestDTO;
import com.studylib.modules.practice.entity.PracticePaperEntity;
import com.studylib.modules.practice.mapper.PracticePaperMapper;
import com.studylib.modules.practice.service.AdminPracticePaperService;
import com.studylib.modules.practice.support.PracticePaperInsightStore;
import com.studylib.modules.practice.vo.PracticeAnalysisVO;
import com.studylib.modules.practice.vo.PracticePaperDetailVO;
import com.studylib.modules.practice.vo.PracticePeerComparisonVO;
import com.studylib.modules.practice.vo.PracticePaperVO;
import com.studylib.modules.practice.vo.WrongQuestionVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminPracticePaperServiceImpl implements AdminPracticePaperService {

  private final PracticePaperMapper practicePaperMapper;
  private final AuditLogRepository auditLogRepository;
  private final PracticePaperAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;
  private final PracticePaperInsightStore practicePaperInsightStore;

  public AdminPracticePaperServiceImpl(PracticePaperMapper practicePaperMapper, AuditLogRepository auditLogRepository,
      PracticePaperAssembler assembler, SoftDeleteHelper softDeleteHelper, PracticePaperInsightStore practicePaperInsightStore) {
    this.practicePaperMapper = practicePaperMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
    this.practicePaperInsightStore = practicePaperInsightStore;
  }

  @Override
  public PageResponse<PracticePaperVO> getPaperList(PracticePaperQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = practicePaperMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        practicePaperMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public PracticePaperVO savePaper(PracticePaperSaveRequestDTO request) {
    Long paperId = request.getPaperId() != null && request.getPaperId() > 0 ? request.getPaperId() : request.getId();
    boolean creating = paperId == null || paperId <= 0;
    PracticePaperEntity entity = creating ? new PracticePaperEntity() : practicePaperMapper.selectById(paperId);
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Practice paper not found");
    }

    entity.setPaperId(paperId);
    entity.setPaperName(request.getPaperName().trim());
    entity.setPaperType(request.getPaperType().trim());
    entity.setCourseName(request.getCourseName().trim());
    entity.setRuleDesc(entity.getRuleDesc() == null ? "" : entity.getRuleDesc());
    entity.setQuestionCount(request.getQuestionCount());
    entity.setStatus(request.getStatus().trim());
    entity.setAvgScore(request.getAvgScore());
    entity.setUpdateTime(DateTimeUtils.now());

    if (creating) {
      practicePaperMapper.insert(entity);
    } else {
      practicePaperMapper.update(entity);
    }

    PracticePaperVO saved = assembler.toVO(practicePaperMapper.selectById(entity.getPaperId()));
    auditLogRepository.appendOperationLog("练习试卷", creating ? "新增" : "编辑", (creating ? "创建试卷 " : "更新试卷 ") + saved.paperName());
    return saved;
  }

  @Override
  public PracticePaperDetailVO getPaperDetail(Long paperId) {
    return practicePaperInsightStore.getPaperDetail(requirePaper(paperId));
  }

  @Override
  public PracticePaperDetailVO savePaperDetail(Long paperId, PracticePaperDetailSaveRequestDTO request) {
    PracticePaperDetailVO saved = practicePaperInsightStore.savePaperDetail(requirePaper(paperId), request);
    auditLogRepository.appendOperationLog("练习试卷", "编辑", "更新试卷 " + paperId + " 的组卷明细");
    return saved;
  }

  @Override
  public PracticeAnalysisVO getPracticeAnalysis(Long paperId) {
    return practicePaperInsightStore.getPracticeAnalysis(requirePaper(paperId));
  }

  @Override
  public List<WrongQuestionVO> getWrongQuestionAnalysis(Long paperId) {
    return practicePaperInsightStore.getWrongQuestions(requirePaper(paperId));
  }

  @Override
  public List<PracticePeerComparisonVO> getPeerComparison(Long paperId) {
    return practicePaperInsightStore.getPeerComparison(requirePaper(paperId));
  }

  @Override
  public SuccessResponseVO deletePaper(Long paperId) {
    PracticePaperEntity entity = requirePaper(paperId);
    return softDeleteHelper.softDelete(
        entity,
        item -> practicePaperMapper.deleteById(paperId, item.getUpdatedAt(), item.getUpdatedBy()),
        "练习试卷",
        "删除试卷 " + entity.getPaperName()
    );
  }

  private PracticePaperEntity requirePaper(Long paperId) {
    PracticePaperEntity entity = practicePaperMapper.selectById(paperId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Practice paper not found");
    }
    return entity;
  }

}
