package com.studylib.modules.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class AdminCourseChapterSaveRequestDTO {

  private Long id;

  @NotBlank(message = "title is required")
  private String title;

  @Min(value = 1, message = "durationMinutes must be greater than 0")
  private Integer durationMinutes;

  @NotBlank(message = "status is required")
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
