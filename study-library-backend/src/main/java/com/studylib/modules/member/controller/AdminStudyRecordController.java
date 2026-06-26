package com.studylib.modules.member.controller;

import com.studylib.common.security.RequirePermission;
import com.studylib.common.vo.ApiResponse;
import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.member.dto.StudyRecordQueryDTO;
import com.studylib.modules.member.service.AdminStudyRecordService;
import com.studylib.modules.member.vo.StudyRecordVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/study-records")
@RequirePermission("member:manage")
public class AdminStudyRecordController {

  private final AdminStudyRecordService adminStudyRecordService;

  public AdminStudyRecordController(AdminStudyRecordService adminStudyRecordService) {
    this.adminStudyRecordService = adminStudyRecordService;
  }

  @GetMapping
  public ApiResponse<PageResponse<StudyRecordVO>> getStudyRecordList(StudyRecordQueryDTO query) {
    return ApiResponse.success(adminStudyRecordService.getStudyRecordList(query));
  }

  @DeleteMapping("/{recordId}")
  public ApiResponse<SuccessResponseVO> deleteStudyRecord(@PathVariable Long recordId) {
    return ApiResponse.success(adminStudyRecordService.deleteStudyRecord(recordId));
  }
}
