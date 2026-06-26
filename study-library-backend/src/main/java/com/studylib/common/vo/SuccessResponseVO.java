package com.studylib.common.vo;

public record SuccessResponseVO(Boolean success) {

  public static SuccessResponseVO ok() {
    return new SuccessResponseVO(Boolean.TRUE);
  }
}
