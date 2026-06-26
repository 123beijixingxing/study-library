package com.studylib.modules.message.dto;

import java.util.List;

public class AdminMessageCreateRequestDTO {
    public String title;
    public String messageType;
    public String content;
    public String receiverType;
    public List<Long> receiverIds;
}
