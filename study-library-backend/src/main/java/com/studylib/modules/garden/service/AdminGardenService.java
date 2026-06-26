package com.studylib.modules.garden.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.garden.dto.GardenAuditRequestDTO;
import com.studylib.modules.garden.dto.GardenContentQueryDTO;
import com.studylib.modules.garden.vo.GardenContentVO;

public interface AdminGardenService {

  PageResponse<GardenContentVO> getContentList(GardenContentQueryDTO query);

  GardenContentVO auditContent(Long contentId, GardenAuditRequestDTO request);

  SuccessResponseVO deleteContent(Long contentId);
}
