package com.studylib.modules.course.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeKeyword;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.common.vo.PageResponse;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.course.assembler.CourseEvaluationAssembler;
import com.studylib.modules.course.dto.CourseEvaluationQueryDTO;
import com.studylib.modules.course.dto.CourseEvaluationStatusUpdateRequestDTO;
import com.studylib.modules.course.entity.CourseEvaluationEntity;
import com.studylib.modules.course.mapper.CourseEvaluationMapper;
import com.studylib.modules.course.service.AdminCourseEvaluationService;
import com.studylib.modules.course.vo.CourseEvaluationVO;
import org.springframework.stereotype.Service;

@Service
public class AdminCourseEvaluationServiceImpl implements AdminCourseEvaluationService {

  private final CourseEvaluationMapper courseEvaluationMapper;
  private final AuditLogRepository auditLogRepository;
  private final CourseEvaluationAssembler assembler;
  private final AuditFieldHelper auditFieldHelper;

  public AdminCourseEvaluationServiceImpl(CourseEvaluationMapper courseEvaluationMapper, AuditLogRepository auditLogRepository,
      CourseEvaluationAssembler assembler, AuditFieldHelper auditFieldHelper) {
    this.courseEvaluationMapper = courseEvaluationMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.auditFieldHelper = auditFieldHelper;
  }

  @Override
  public PageResponse<CourseEvaluationVO> getEvaluationList(CourseEvaluationQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = courseEvaluationMapper.countPage(normalizeKeyword(query.getKeyword()), normalizeFilter(query.getStatus()), normalizeFilter(query.getCourseName()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        courseEvaluationMapper.selectPage(
                normalizeKeyword(query.getKeyword()),
                normalizeFilter(query.getStatus()),
                normalizeFilter(query.getCourseName()),
                offset(currentPageNum, currentPageSize),
                currentPageSize
            )
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public CourseEvaluationVO updateEvaluationStatus(Long evaluationId, CourseEvaluationStatusUpdateRequestDTO request) {
    CourseEvaluationEntity entity = courseEvaluationMapper.selectById(evaluationId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Course evaluation not found");
    }
    String status = request.getStatus().trim();
    entity.setStatus(status);
    if ("deleted".equalsIgnoreCase(status)) {
      auditFieldHelper.fillForSoftDelete(entity);
    } else {
      entity.setDeleted(Boolean.FALSE);
      auditFieldHelper.fillForUpdate(entity);
      entity.setDeleted(Boolean.FALSE);
    }
    courseEvaluationMapper.updateStatus(evaluationId, status, entity.getDeleted(), entity.getUpdatedAt(), entity.getUpdatedBy());
    CourseEvaluationVO saved = assembler.toVO(courseEvaluationMapper.selectById(evaluationId));
    auditLogRepository.appendOperationLog("课程评价", "状态变更", "课程评价 " + evaluationId + " 状态更新为 " + saved.status());
    return saved;
  }

}
