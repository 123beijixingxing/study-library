package com.studylib.modules.service.vo;

import java.time.LocalDateTime;

public record ServiceTicketVO(Long id, String userName, String latestMessage, String priorityLevel, String status, LocalDateTime updateTime) {
}
