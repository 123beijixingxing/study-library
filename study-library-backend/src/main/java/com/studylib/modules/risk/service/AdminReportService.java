package com.studylib.modules.risk.service;

import com.studylib.common.vo.PageResponse;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;
import com.studylib.modules.risk.dto.ReportQueryDTO;
import com.studylib.modules.risk.vo.ReportVO;

public interface AdminReportService {

  PageResponse<ReportVO> getReportList(ReportQueryDTO query);

  ReportVO handleReport(Long reportId, ReportHandleRequestDTO request);
}
