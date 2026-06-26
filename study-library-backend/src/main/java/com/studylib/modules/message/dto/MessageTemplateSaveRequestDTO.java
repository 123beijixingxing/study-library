package com.studylib.modules.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageTemplateSaveRequestDTO {

  private Long templateId;

  @NotBlank(message = "templateName is required")
  private String templateName;

  @NotBlank(message = "messageType is required")
  private String messageType;

  @NotBlank(message = "titleTemplate is required")
  private String titleTemplate;

  @NotBlank(message = "contentTemplate is required")
  private String contentTemplate;

  @NotEmpty(message = "channelList is required")
  private List<String> channelList;

  @NotBlank(message = "status is required")
  private String status;

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
}
