package com.studylib.modules.message.vo;

import java.time.LocalDateTime;
import java.util.List;

public record MessageTemplateVO(Long templateId, String templateName, String messageType, String titleTemplate, String contentTemplate,
    List<String> channelList, String status, LocalDateTime updateTime) {
}
