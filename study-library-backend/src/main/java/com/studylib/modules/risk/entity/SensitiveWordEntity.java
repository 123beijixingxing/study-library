package com.studylib.modules.risk.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.time.LocalDateTime;

public class SensitiveWordEntity extends BaseAuditEntity {

  private Long wordId;
  private String word;
  private String replaceText;
  private String status;
  private LocalDateTime updateTime;

  public Long getWordId() {
    return wordId;
  }

  public void setWordId(Long wordId) {
    this.wordId = wordId;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getReplaceText() {
    return replaceText;
  }

  public void setReplaceText(String replaceText) {
    this.replaceText = replaceText;
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
