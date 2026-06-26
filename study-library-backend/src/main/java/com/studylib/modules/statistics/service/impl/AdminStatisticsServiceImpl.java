package com.studylib.modules.statistics.service.impl;

import com.studylib.modules.course.mapper.CourseMapper;
import com.studylib.modules.garden.mapper.GardenContentMapper;
import com.studylib.modules.question.mapper.QuestionMapper;
import com.studylib.modules.statistics.service.AdminStatisticsService;
import com.studylib.modules.statistics.support.StatisticsMetricStore;
import com.studylib.modules.statistics.vo.StatisticsOverviewVO;
import com.studylib.modules.statistics.vo.StatisticsTrendPointVO;
import com.studylib.modules.user.mapper.AdminUserMapper;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

  private final AdminUserMapper adminUserMapper;
  private final CourseMapper courseMapper;
  private final QuestionMapper questionMapper;
  private final GardenContentMapper gardenContentMapper;
  private final StatisticsMetricStore statisticsMetricStore;

  public AdminStatisticsServiceImpl(
      AdminUserMapper adminUserMapper,
      CourseMapper courseMapper,
      QuestionMapper questionMapper,
      GardenContentMapper gardenContentMapper,
      StatisticsMetricStore statisticsMetricStore
  ) {
    this.adminUserMapper = adminUserMapper;
    this.courseMapper = courseMapper;
    this.questionMapper = questionMapper;
    this.gardenContentMapper = gardenContentMapper;
    this.statisticsMetricStore = statisticsMetricStore;
  }

  @Override
  public StatisticsOverviewVO getOverview() {
    return new StatisticsOverviewVO(userTotal(), courseTotal(), questionTotal(), interactionTotal());
  }

  @Override
  public List<StatisticsTrendPointVO> getTrends() {
    return statisticsMetricStore.getRecentMetrics(7).stream()
        .map(item -> new StatisticsTrendPointVO(
            item.getMetricDate().format(DATE_FORMATTER),
            item.getNewUsers(),
            item.getActiveUsers(),
            item.getCourseViews(),
            item.getContentInteractions()
        ))
        .toList();
  }

  private long userTotal() {
    Long count = adminUserMapper.countPage(null, null, null);
    return count == null ? 0L : count;
  }

  private long courseTotal() {
    Long count = courseMapper.countPage(null, null, null);
    return count == null ? 0L : count;
  }

  private long questionTotal() {
    Long count = questionMapper.countAll();
    return count == null ? 0L : count;
  }

  private long interactionTotal() {
    return gardenContentMapper.selectPage(null, null, 0, 1000).stream()
        .mapToLong(item -> safeLong(item.getLikeCount()) + safeLong(item.getCommentCount()))
        .sum();
  }

  private long safeLong(Integer value) {
    return value == null ? 0L : value.longValue();
  }
}
