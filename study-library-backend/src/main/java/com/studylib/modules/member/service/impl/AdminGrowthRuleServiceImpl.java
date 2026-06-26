package com.studylib.modules.member.service.impl;

import com.studylib.common.constants.ErrorCodeConstants;
import com.studylib.common.exception.BusinessException;
import com.studylib.infrastructure.persistence.AuditLogRepository;
import com.studylib.modules.member.assembler.GrowthRuleAssembler;
import com.studylib.modules.member.dto.GrowthRuleSaveRequestDTO;
import com.studylib.modules.member.entity.GrowthRuleEntity;
import com.studylib.modules.member.mapper.GrowthRuleMapper;
import com.studylib.modules.member.service.AdminGrowthRuleService;
import com.studylib.modules.member.vo.GrowthRuleVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminGrowthRuleServiceImpl implements AdminGrowthRuleService {

  private final GrowthRuleMapper growthRuleMapper;
  private final AuditLogRepository auditLogRepository;
  private final GrowthRuleAssembler assembler;

  public AdminGrowthRuleServiceImpl(GrowthRuleMapper growthRuleMapper, AuditLogRepository auditLogRepository,
      GrowthRuleAssembler assembler) {
    this.growthRuleMapper = growthRuleMapper;
    this.auditLogRepository = auditLogRepository;
    this.assembler = assembler;
  }

  @Override
  public List<GrowthRuleVO> getRuleList() {
    return growthRuleMapper.selectList().stream().map(assembler::toVO).toList();
  }

  @Override
  public GrowthRuleVO saveRule(GrowthRuleSaveRequestDTO request) {
    GrowthRuleEntity entity = growthRuleMapper.selectById(request.getRuleId());
    if (entity == null) {
      throw new BusinessException(ErrorCodeConstants.NOT_FOUND, "Growth rule not found");
    }

    entity.setRuleName(request.getRuleName().trim());
    entity.setTriggerType(request.getTriggerType().trim());
    entity.setGrowthValue(request.getGrowthValue());
    entity.setDailyLimit(request.getDailyLimit());
    entity.setStatus(request.getStatus().trim());
    growthRuleMapper.update(entity);

    GrowthRuleVO saved = assembler.toVO(growthRuleMapper.selectById(request.getRuleId()));
    auditLogRepository.appendOperationLog("成长规则", "编辑", "更新规则 " + saved.ruleName());
    return saved;
  }
}
