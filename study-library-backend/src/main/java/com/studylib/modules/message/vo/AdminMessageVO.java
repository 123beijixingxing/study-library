package com.studylib.modules.message.vo;

import java.time.LocalDateTime;

public record AdminMessageVO(Long id, String title, String messageType, String receiverType, String sendStatus, LocalDateTime sendTime) {
}
