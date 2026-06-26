package com.studylib.modules.statistics.service;

import com.studylib.modules.statistics.vo.StatisticsOverviewVO;
import com.studylib.modules.statistics.vo.StatisticsTrendPointVO;
import java.util.List;

public interface AdminStatisticsService {

  StatisticsOverviewVO getOverview();

  List<StatisticsTrendPointVO> getTrends();
}
