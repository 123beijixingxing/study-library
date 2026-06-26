package com.studylib.modules.statistics.service.impl;

import com.studylib.modules.course.mapper.CourseMapper;
import com.studylib.modules.member.mapper.MemberMapper;
import com.studylib.modules.statistics.service.AdminDashboardService;
import com.studylib.modules.statistics.support.StatisticsMetricStore;
import com.studylib.modules.statistics.vo.DashboardSummaryVO;
import com.studylib.modules.statistics.vo.DashboardTrendPointVO;
import com.studylib.modules.user.mapper.AdminUserMapper;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

  private final AdminUserMapper adminUserMapper;
  private final CourseMapper courseMapper;
  private final MemberMapper memberMapper;
  private final StatisticsMetricStore statisticsMetricStore;

  public AdminDashboardServiceImpl(
      AdminUserMapper adminUserMapper,
      CourseMapper courseMapper,
      MemberMapper memberMapper,
      StatisticsMetricStore statisticsMetricStore
  ) {
    this.adminUserMapper = adminUserMapper;
    this.courseMapper = courseMapper;
    this.memberMapper = memberMapper;
    this.statisticsMetricStore = statisticsMetricStore;
  }

  @Override
  public DashboardSummaryVO getSummary() {
    int activeToday = statisticsMetricStore.getLatestMetric().getActiveUsers();
    int userTotal = Math.toIntExact(adminUserCount());
    int courseTotal = Math.toIntExact(courseCount());
    int memberTotal = Math.toIntExact(memberMapper.selectList(null, null).stream()
        .filter(item -> "active".equalsIgnoreCase(item.getStatus()) || "expiring".equalsIgnoreCase(item.getStatus()))
        .count());
    return new DashboardSummaryVO(userTotal, courseTotal, activeToday, memberTotal);
  }

  @Override
  public List<DashboardTrendPointVO> getUserTrend() {
    return statisticsMetricStore.getRecentMetrics(7).stream()
        .map(item -> new DashboardTrendPointVO(item.getMetricDate().format(DATE_FORMATTER), item.getNewUsers()))
        .toList();
  }

  private long adminUserCount() {
    Long count = adminUserMapper.countPage(null, null, null);
    return count == null ? 0L : count;
  }

  private long courseCount() {
    Long count = courseMapper.countPage(null, null, null);
    return count == null ? 0L : count;
  }
}
