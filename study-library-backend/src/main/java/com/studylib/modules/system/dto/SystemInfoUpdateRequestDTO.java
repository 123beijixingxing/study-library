package com.studylib.modules.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInfoUpdateRequestDTO {

  @NotBlank(message = "systemName is required")
  private String systemName;

  @NotBlank(message = "version is required")
  private String version;

  @NotBlank(message = "copyright is required")
  private String copyright;

  @NotBlank(message = "contactInfo is required")
  private String contactInfo;

  public String getSystemName() {
    return systemName;
  }

  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getContactInfo() {
    return contactInfo;
  }

  public void setContactInfo(String contactInfo) {
    this.contactInfo = contactInfo;
  }
}
