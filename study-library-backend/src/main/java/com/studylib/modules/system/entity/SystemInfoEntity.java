package com.studylib.modules.system.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;

public class SystemInfoEntity extends BaseAuditEntity {

  private String systemName;
  private String version;
  private String copyright;
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
