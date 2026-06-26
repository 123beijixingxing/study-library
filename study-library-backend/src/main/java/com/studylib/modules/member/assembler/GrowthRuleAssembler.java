package com.studylib.modules.member.assembler;

import com.studylib.modules.member.entity.GrowthRuleEntity;
import com.studylib.modules.member.vo.GrowthRuleVO;
import org.springframework.stereotype.Component;

@Component
public class GrowthRuleAssembler {

  public GrowthRuleVO toVO(GrowthRuleEntity entity) {
    return new GrowthRuleVO(entity.getRuleId(), entity.getRuleName(), entity.getTriggerType(), entity.getGrowthValue(), entity.getDailyLimit(), entity.getStatus());
  }
}
