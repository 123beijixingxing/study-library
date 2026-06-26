package com.studylib.modules.member.vo;

import java.util.List;

public record MemberPackageVO(Long packageId, String packageName, Double price, Integer durationDays, List<String> benefitList, String status, Integer sortNo) {
}
