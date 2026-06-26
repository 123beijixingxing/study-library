package com.studylib.modules.member.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.member.assembler.StudyRecordAssembler;
import com.studylib.modules.member.dto.StudyRecordQueryDTO;
import com.studylib.modules.member.entity.StudyRecordEntity;
import com.studylib.modules.member.mapper.StudyRecordMapper;
import com.studylib.modules.member.service.AdminStudyRecordService;
import com.studylib.modules.member.vo.StudyRecordVO;
import org.springframework.stereotype.Service;

@Service
public class AdminStudyRecordServiceImpl implements AdminStudyRecordService {

  private final StudyRecordMapper studyRecordMapper;
  private final AuditLogRepository auditLogRepository;
  private final StudyRecordAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminStudyRecordServiceImpl(StudyRecordMapper studyRecordMapper, AuditLogRepository auditLogRepository,
      StudyRecordAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.studyRecordMapper = studyRecordMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<StudyRecordVO> getStudyRecordList(StudyRecordQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = studyRecordMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        studyRecordMapper.selectPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public SuccessResponseVO deleteStudyRecord(Long recordId) {
    StudyRecordEntity entity = studyRecordMapper.selectById(recordId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Study record not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> studyRecordMapper.deleteById(recordId, item.getUpdatedAt(), item.getUpdatedBy()),
        "学习记录",
        "删除学习记录 " + entity.getRecordId()
    );
  }

}
