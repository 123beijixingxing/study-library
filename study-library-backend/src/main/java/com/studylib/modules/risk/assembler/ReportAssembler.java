package com.studylib.modules.risk.assembler;

import com.studylib.modules.risk.entity.ReportEntity;
import com.studylib.modules.risk.vo.ReportVO;
import org.springframework.stereotype.Component;

@Component
public class ReportAssembler {

  public ReportVO toVO(ReportEntity entity) {
    return new ReportVO(
        entity.getReportId(),
        entity.getReportType(),
        entity.getTargetId(),
        entity.getTargetTitle(),
        entity.getReporterName(),
        entity.getStatus(),
        entity.getReportTime(),
        entity.getHandleResult(),
        entity.getHandleRemark()
    );
  }
}
