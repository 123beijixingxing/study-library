package com.studylib.modules.member.service.impl;

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
import com.studylib.modules.member.assembler.MemberPackageAssembler;
import com.studylib.modules.member.dto.MemberPackageQueryDTO;
import com.studylib.modules.member.dto.MemberPackageSaveRequestDTO;
import com.studylib.modules.member.entity.MemberPackageEntity;
import com.studylib.modules.member.mapper.MemberPackageMapper;
import com.studylib.modules.member.service.AdminMemberPackageService;
import com.studylib.modules.member.vo.MemberPackageVO;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class AdminMemberPackageServiceImpl implements AdminMemberPackageService {

  private final MemberPackageMapper memberPackageMapper;
  private final AuditLogRepository auditLogRepository;
  private final MemberPackageAssembler assembler;
  private final SoftDeleteHelper softDeleteHelper;

  public AdminMemberPackageServiceImpl(MemberPackageMapper memberPackageMapper, AuditLogRepository auditLogRepository,
      MemberPackageAssembler assembler, SoftDeleteHelper softDeleteHelper) {
    this.memberPackageMapper = memberPackageMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
    this.softDeleteHelper = softDeleteHelper;
  }

  @Override
  public PageResponse<MemberPackageVO> getPackageList(MemberPackageQueryDTO query) {
    int currentPageNum = pageNum(query);
    int currentPageSize = pageSize(query);
    Long total = memberPackageMapper.countPage(normalizeFilter(query.getStatus()));
    return build(
        currentPageNum,
        currentPageSize,
        total,
        memberPackageMapper.selectPage(normalizeFilter(query.getStatus()), offset(currentPageNum, currentPageSize), currentPageSize)
            .stream()
            .map(assembler::toVO)
            .toList()
    );
  }

  @Override
  public MemberPackageVO savePackage(MemberPackageSaveRequestDTO request) {
    boolean creating = request.getPackageId() == null || request.getPackageId() <= 0;
    MemberPackageEntity entity = creating ? new MemberPackageEntity() : memberPackageMapper.selectById(request.getPackageId());
    if (!creating && entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Member package not found");
    }

    entity.setPackageId(request.getPackageId());
    entity.setPackageName(request.getPackageName().trim());
    entity.setPrice(request.getPrice());
    entity.setDurationDays(request.getDurationDays());
    entity.setBenefitList(new ArrayList<>(request.getBenefitList()));
    entity.setStatus(request.getStatus().trim());
    entity.setSortNo(request.getSortNo());

    if (creating) {
      memberPackageMapper.insert(entity);
    } else {
      memberPackageMapper.update(entity);
    }

    MemberPackageVO saved = assembler.toVO(memberPackageMapper.selectById(entity.getPackageId()));
    auditLogRepository.appendOperationLog("会员套餐", creating ? "新增" : "编辑", (creating ? "创建套餐 " : "更新套餐 ") + saved.packageName());
    return saved;
  }

  @Override
  public SuccessResponseVO deletePackage(Long packageId) {
    MemberPackageEntity entity = memberPackageMapper.selectById(packageId);
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Member package not found");
    }
    return softDeleteHelper.softDelete(
        entity,
        item -> memberPackageMapper.deleteById(packageId, item.getUpdatedAt(), item.getUpdatedBy()),
        "会员套餐",
        "删除套餐 " + entity.getPackageName()
    );
  }

}
