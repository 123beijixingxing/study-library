package com.studylib.modules.member.mapper;

import com.studylib.modules.member.entity.GrowthRuleEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrowthRuleMapper {

  GrowthRuleEntity selectById(@Param("ruleId") Long ruleId);

  List<GrowthRuleEntity> selectList();

  Integer update(GrowthRuleEntity entity);
}
