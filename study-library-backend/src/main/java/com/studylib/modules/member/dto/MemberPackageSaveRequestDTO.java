package com.studylib.modules.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberPackageSaveRequestDTO {

  private Long packageId;

  @NotBlank(message = "packageName is required")
  private String packageName;

  @NotNull(message = "price is required")
  private Double price;

  @NotNull(message = "durationDays is required")
  private Integer durationDays;

  @NotEmpty(message = "benefitList is required")
  private List<String> benefitList;

  @NotBlank(message = "status is required")
  private String status;

  @NotNull(message = "sortNo is required")
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
