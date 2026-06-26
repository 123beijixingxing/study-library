package com.studylib.modules.operation.service.impl;

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
import com.studylib.modules.operation.assembler.RecommendSlotAssembler;
import com.studylib.modules.operation.dto.RecommendSlotQueryDTO;
import com.studylib.modules.operation.dto.RecommendSlotSaveRequestDTO;
import com.studylib.modules.operation.entity.RecommendSlotEntity;
import com.studylib.modules.operation.mapper.RecommendSlotMapper;
import com.studylib.modules.operation.service.AdminRecommendSlotService;
import com.studylib.modules.operation.vo.RecommendSlotVO;
import org.springframework.stereotype.Service;

@Service
public class AdminRecommendSlotServiceImpl implements AdminRecommendSlotService {

  private final RecommendSlotMapper recommendSlotMapper;
  private final AuditLogRepository auditLogRepository;
  private final RecommendSlotAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminRecommendSlotServiceImpl(RecommendSlotMapper recommendSlotMapper, AuditLogRepository auditLogRepository,
      RecommendSlotAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.recommendSlotMapper = recommendSlotMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<RecommendSlotVO> getSlotList(RecommendSlotQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = recommendSlotMapper.countPage(normalizeFilter(query.getPageCode()), normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        recommendSlotMapper.selectPage(normalizeFilter(query.getPageCode()), normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public RecommendSlotVO saveSlot(RecommendSlotSaveRequestDTO request) {
    boolean creating = request.getSlotId() == null || request.getSlotId() <= 0;
    RecommendSlotEntity entity = creating ? new RecommendSlotEntity() : recommendSlotMapper.selectById(request.getSlotId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Recommend slot not found");
    }

    entity.setSlotId(request.getSlotId());
    entity.setSlotName(request.getSlotName().trim());
    entity.setPageCode(request.getPageCode().trim());
    entity.setResourceType(request.getResourceType().trim());
    entity.setTargetTitle(request.getTargetTitle().trim());
    entity.setSortNo(request.getSortNo());
    entity.setStatus(request.getStatus().trim());

    if (creating) {
      recommendSlotMapper.insert(entity);
    } else {
      recommendSlotMapper.update(entity);
    }

    RecommendSlotVO saved = assembler.toVO(recommendSlotMapper.selectById(entity.getSlotId()));
    auditLogRepository.appendOperationLog("推荐位配置", creating ? "新增" : "编辑", (creating ? "创建推荐位 " : "更新推荐位 ") + saved.slotName());
    return saved;
  }

  @Override
  public SuccessResponseVO deleteSlot(Long slotId) {
    RecommendSlotEntity entity = recommendSlotMapper.selectById(slotId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Recommend slot not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> recommendSlotMapper.deleteById(slotId, item.getUpdatedAt(), item.getUpdatedBy()),
        "推荐位配置",
        "删除推荐位 " + entity.getSlotName()
    );
  }

}
