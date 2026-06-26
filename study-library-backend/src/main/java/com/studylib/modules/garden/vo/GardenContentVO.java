package com.studylib.modules.garden.vo;

import java.time.LocalDateTime;

public record GardenContentVO(
    Long id,
    String contentType,
    String sourceName,
    String authorName,
    String contentText,
    Integer likeCount,
    Integer commentCount,
    String auditStatus,
    LocalDateTime publishTime
) {
}
