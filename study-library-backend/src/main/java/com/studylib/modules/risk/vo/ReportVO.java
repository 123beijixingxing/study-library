package com.studylib.modules.risk.vo;

import java.time.LocalDateTime;

public record ReportVO(
    Long reportId,
    String reportType,
    Long targetId,
    String targetTitle,
    String reporterName,
    String status,
    LocalDateTime reportTime,
    String handleResult,
    String handleRemark
) {
}
