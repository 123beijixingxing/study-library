package com.studylib.modules.risk.mapper;

import com.studylib.modules.risk.entity.ReportEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReportMapper {

  ReportEntity selectById(@Param("reportId") Long reportId);

  Long countPage(@Param("keyword") String keyword, @Param("reportType") String reportType, @Param("status") String status);

  List<ReportEntity> selectPage(@Param("keyword") String keyword, @Param("reportType") String reportType, @Param("status") String status,
      @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

  Integer update(ReportEntity entity);
}
