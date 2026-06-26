package com.studylib.modules.practice.vo;

import java.util.List;

public record PracticeAnalysisVO(
    long paperId,
    String paperName,
    int totalSubmitCount,
    int avgScore,
    int passRate,
    List<PracticeScoreDistributionVO> scoreDistribution,
    List<PracticeTrendPointVO> trendList,
    List<PracticeHourPointVO> hourlyHeat
) {
}
