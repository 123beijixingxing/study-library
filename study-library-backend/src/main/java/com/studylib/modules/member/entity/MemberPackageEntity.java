package com.studylib.modules.member.entity;

import com.studylib.common.persistence.model.BaseAuditEntity;
import java.util.ArrayList;
import java.util.List;

public class MemberPackageEntity extends BaseAuditEntity {

  private Long packageId;
  private String packageName;
  private Double price;
  private Integer durationDays;
  private List<String> benefitList = new ArrayList<>();
  private String status;
  private Integer sortNo;

  public Long getPackageId() {
    return packageId;
  }

  public void setPackageId(Long packageId) {
    this.packageId = packageId;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(Integer durationDays) {
    this.durationDays = durationDays;
  }

  public List<String> getBenefitList() {
    return benefitList;
  }

  public void setBenefitList(List<String> benefitList) {
    this.benefitList = benefitList;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getSortNo() {
    return sortNo;
  }

  public void setSortNo(Integer sortNo) {
    this.sortNo = sortNo;
  }
}
