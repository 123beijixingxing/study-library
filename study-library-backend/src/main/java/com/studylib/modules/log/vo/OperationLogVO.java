package com.studylib.modules.log.vo;

import java.time.LocalDateTime;

public record OperationLogVO(
    Long id,
    LocalDateTime operateTime,
    String operatorName,
    String operationModule,
    String operationType,
    String operationContent,
    String ip
) {
}
