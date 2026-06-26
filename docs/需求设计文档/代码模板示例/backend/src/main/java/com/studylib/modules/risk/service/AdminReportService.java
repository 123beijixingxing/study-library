package com.studylib.modules.risk.service;

import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;

public interface AdminReportService {
    SuccessResponseVO handleReport(Long reportId, ReportHandleRequestDTO request);
}
