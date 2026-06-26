package com.studylib.modules.risk.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SensitiveWordSaveRequestDTO {

  private Long wordId;

  @NotBlank(message = "word is required")
  private String word;

  @NotBlank(message = "replaceText is required")
  private String replaceText;

  @NotBlank(message = "status is required")
  private String status;

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
}
