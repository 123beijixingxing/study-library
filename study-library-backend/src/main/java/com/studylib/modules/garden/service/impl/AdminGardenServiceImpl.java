package com.studylib.modules.garden.service.impl;

import static com.studylib.common.persistence.support.PageQueryHelper.build;
import static com.studylib.common.persistence.support.PageQueryHelper.normalizeFilter;
import static com.studylib.common.persistence.support.PageQueryHelper.offset;
import static com.studylib.common.persistence.support.PageQueryHelper.pageNum;
import static com.studylib.common.persistence.support.PageQueryHelper.pageSize;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.common.persistence.support.SoftDeleteHelper;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.garden.assembler.GardenContentAssembler;
import com.studylib.modules.garden.dto.GardenAuditRequestDTO;
import com.studylib.modules.garden.dto.GardenContentQueryDTO;
import com.studylib.modules.garden.entity.GardenContentEntity;
import com.studylib.modules.garden.mapper.GardenContentMapper;
import com.studylib.modules.garden.service.AdminGardenService;
import com.studylib.modules.garden.vo.GardenContentVO;
import org.springframework.stereotype.Service;

@Service
public class AdminGardenServiceImpl implements AdminGardenService {

  private final GardenContentMapper gardenContentMapper;
  private final AuditLogRepository auditLogRepository;
  private final GardenContentAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminGardenServiceImpl(GardenContentMapper gardenContentMapper, AuditLogRepository auditLogRepository,
      GardenContentAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.gardenContentMapper = gardenContentMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<GardenContentVO> getContentList(GardenContentQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = gardenContentMapper.countPage(normalizeFilter(query.getContentType()), normalizeFilter(query.getAuditStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        gardenContentMapper.selectPage(normalizeFilter(query.getContentType()), normalizeFilter(query.getAuditStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public GardenContentVO auditContent(Long contentId, GardenAuditRequestDTO request) {
    GardenContentEntity entity = gardenContentMapper.selectById(contentId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Garden content not found");
    }
    entity.setAuditStatus(request.getAuditStatus().trim());
    gardenContentMapper.update(entity);
    GardenContentVO saved = assembler.toVO(gardenContentMapper.selectById(contentId));
    auditLogRepository.appendOperationLog("园地管理", "状态变更", "内容 " + contentId + " 审核状态改为 " + saved.auditStatus());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteContent(Long contentId) {
    GardenContentEntity entity = gardenContentMapper.selectById(contentId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Garden content not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> gardenContentMapper.deleteById(contentId, item.getUpdatedAt(), item.getUpdatedBy()),
        "园地管理",
        "删除内容 " + entity.getSourceName()
    );
  }

}
