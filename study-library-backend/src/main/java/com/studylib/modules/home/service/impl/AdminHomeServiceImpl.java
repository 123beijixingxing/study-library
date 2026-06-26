package com.studylib.modules.home.service.impl;

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
import com.studylib.modules.home.assembler.HomeSectionAssembler;
import com.studylib.modules.home.dto.HomeSectionQueryDTO;
import com.studylib.modules.home.dto.HomeSectionUpdateRequestDTO;
import com.studylib.modules.home.entity.HomeSectionEntity;
import com.studylib.modules.home.mapper.HomeSectionMapper;
import com.studylib.modules.home.service.AdminHomeService;
import com.studylib.modules.home.vo.HomeSectionVO;
import org.springframework.stereotype.Service;

@Service
public class AdminHomeServiceImpl implements AdminHomeService {

  private final HomeSectionMapper homeSectionMapper;
  private final AuditLogRepository auditLogRepository;
  private final HomeSectionAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminHomeServiceImpl(HomeSectionMapper homeSectionMapper, AuditLogRepository auditLogRepository, HomeSectionAssembler assembler,
      SoftDeleteHelper softDeleteHelper) {
    this.homeSectionMapper = homeSectionMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<HomeSectionVO> getSectionList(HomeSectionQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = homeSectionMapper.countPage(normalizeFilter(query.getSectionType()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        homeSectionMapper.selectPage(normalizeFilter(query.getSectionType()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public HomeSectionVO updateSection(HomeSectionUpdateRequestDTO request) {
    Long sectionId = request.getId() != null ? request.getId() : request.getConfigId();
    if (sectionId == null || sectionId <= 0) {
      throw new BusinessException(ErrorCodeConstants.INVALID_PARAM, "Section id is required");
    }

    HomeSectionEntity entity = homeSectionMapper.selectById(sectionId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Home section not found");
    }

    if (request.getSectionType() != null && !request.getSectionType().isBlank()) {
      entity.setSectionType(request.getSectionType().trim());
    }
    if (request.getTitle() != null && !request.getTitle().isBlank()) {
      entity.setTitle(request.getTitle().trim());
    }
    if (request.getSummary() != null && !request.getSummary().isBlank()) {
      entity.setSummary(request.getSummary().trim());
    }
    if (request.getHotScore() != null) {
      entity.setHotScore(request.getHotScore());
    }
    if (request.getSortNo() != null) {
      entity.setSortNo(request.getSortNo());
    }
    if (request.getDisplayStatus() != null && !request.getDisplayStatus().isBlank()) {
      entity.setDisplayStatus(request.getDisplayStatus().trim());
    }

    homeSectionMapper.update(entity);
    HomeSectionVO saved = assembler.toVO(homeSectionMapper.selectById(sectionId));
    auditLogRepository.appendOperationLog("首页管理", "编辑", "更新首页配置 " + saved.title());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteSection(Long id) {
    HomeSectionEntity entity = homeSectionMapper.selectById(id);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Home section not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> homeSectionMapper.deleteById(id, item.getUpdatedAt(), item.getUpdatedBy()),
        "首页管理",
        "删除首页配置 " + entity.getTitle()
    );
  }

}
