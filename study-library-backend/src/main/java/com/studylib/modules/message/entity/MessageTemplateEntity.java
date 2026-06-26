package com.studylib.modules.message.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageTemplateEntity extends BaseAuditEntity {

  private Long templateId;
  private String templateName;
  private String messageType;
  private String titleTemplate;
  private String contentTemplate;
  private List<String> channelList = new ArrayList<>();
  private String status;
  private LocalDateTime updateTime;

  public Long getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Long templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getTitleTemplate() {
    return titleTemplate;
  }

  public void setTitleTemplate(String titleTemplate) {
    this.titleTemplate = titleTemplate;
  }

  public String getContentTemplate() {
    return contentTemplate;
  }

  public void setContentTemplate(String contentTemplate) {
    this.contentTemplate = contentTemplate;
  }

  public List<String> getChannelList() {
    return channelList;
  }

  public void setChannelList(List<String> channelList) {
    this.channelList = channelList;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
