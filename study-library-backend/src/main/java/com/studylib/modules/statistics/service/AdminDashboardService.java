package com.studylib.modules.statistics.service;

import com.studylib.modules.statistics.vo.DashboardSummaryVO;
import com.studylib.modules.statistics.vo.DashboardTrendPointVO;
import java.util.List;

public interface AdminDashboardService {

  DashboardSummaryVO getSummary();

  List<DashboardTrendPointVO> getUserTrend();
}
