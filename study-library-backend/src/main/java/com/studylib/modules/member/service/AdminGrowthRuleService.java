package com.studylib.modules.member.service;

import com.studylib.modules.member.dto.GrowthRuleSaveRequestDTO;
import com.studylib.modules.member.vo.GrowthRuleVO;
import java.util.List;

public interface AdminGrowthRuleService {

  List<GrowthRuleVO> getRuleList();

  GrowthRuleVO saveRule(GrowthRuleSaveRequestDTO request);
}
