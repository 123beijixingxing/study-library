package com.studylib.modules.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceTicketReplyRequestDTO {

  @NotNull(message = "ticketId is required")
  private Long ticketId;

  private String replyContent;
  private String status;

  public Long getTicketId() {
    return ticketId;
  }

  public void setTicketId(Long ticketId) {
    this.ticketId = ticketId;
  }

  public String getReplyContent() {
    return replyContent;
  }

  public void setReplyContent(String replyContent) {
    this.replyContent = replyContent;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
