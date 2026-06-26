package com.studylib.modules.member.vo;

public record GrowthRuleVO(Long ruleId, String ruleName, String triggerType, Integer growthValue, Integer dailyLimit, String status) {
}
