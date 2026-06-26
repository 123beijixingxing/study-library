package com.studylib.modules.statistics.vo;

public record StatisticsTrendPointVO(
    String date,
    int newUsers,
    int activeUsers,
    int courseViews,
    int contentInteractions
) {
}
