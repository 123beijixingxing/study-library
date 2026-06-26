package com.studylib.modules.risk.service.impl;

import com.studylib.common.vo.SuccessResponseVO;
import com.studylib.modules.risk.dto.ReportHandleRequestDTO;
import com.studylib.modules.risk.service.AdminReportService;

public class AdminReportServiceImpl implements AdminReportService {
    @Override
    public SuccessResponseVO handleReport(Long reportId, ReportHandleRequestDTO request) {
        throw new UnsupportedOperationException("Implement report status validation and handle result persistence");
    }
}
