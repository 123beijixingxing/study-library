package com.studylib.modules.statistics.support;

import com.studylib.common.persistence.support.AuditFieldHelper;
import com.studylib.modules.course.mapper.CourseMapper;
import com.studylib.modules.garden.mapper.GardenContentMapper;
import com.studylib.modules.question.mapper.QuestionMapper;
import com.studylib.modules.statistics.entity.StatisticsDailyMetricEntity;
import com.studylib.modules.statistics.mapper.StatisticsDailyMetricMapper;
import com.studylib.modules.user.mapper.AdminUserMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StatisticsMetricStore {

  private static final List<Integer> DEFAULT_NEW_USERS = List.of(8, 11, 13, 15, 12, 17, 19);
  private static final List<Integer> DEFAULT_ACTIVE_USERS = List.of(92, 98, 106, 114, 109, 121, 133);
  private static final List<Integer> DEFAULT_COURSE_VIEWS = List.of(240, 268, 301, 326, 315, 348, 372);
  private static final List<Integer> DEFAULT_INTERACTIONS = List.of(18, 24, 31, 37, 34, 42, 49);

  private final StatisticsDailyMetricMapper statisticsDailyMetricMapper;
  private final AuditFieldHelper auditFieldHelper;
  private final AdminUserMapper adminUserMapper;
  private final CourseMapper courseMapper;
  private final QuestionMapper questionMapper;
  private final GardenContentMapper gardenContentMapper;

  public StatisticsMetricStore(
      StatisticsDailyMetricMapper statisticsDailyMetricMapper,
      AuditFieldHelper auditFieldHelper,
      AdminUserMapper adminUserMapper,
      CourseMapper courseMapper,
      QuestionMapper questionMapper,
      GardenContentMapper gardenContentMapper
  ) {
    this.statisticsDailyMetricMapper = statisticsDailyMetricMapper;
    this.auditFieldHelper = auditFieldHelper;
    this.adminUserMapper = adminUserMapper;
    this.courseMapper = courseMapper;
    this.questionMapper = questionMapper;
    this.gardenContentMapper = gardenContentMapper;
  }

  public synchronized List<StatisticsDailyMetricEntity> getRecentMetrics(int limit) {
    bootstrapIfEmpty();
    ensureTodayMetric();
    List<StatisticsDailyMetricEntity> metrics = statisticsDailyMetricMapper.selectRecent(limit);
    List<StatisticsDailyMetricEntity> result = new ArrayList<>(metrics);
    result.sort(Comparator.comparing(StatisticsDailyMetricEntity::getMetricDate));
    return result;
  }

  public synchronized StatisticsDailyMetricEntity getLatestMetric() {
    bootstrapIfEmpty();
    ensureTodayMetric();
    return statisticsDailyMetricMapper.selectLatest();
  }

  private void bootstrapIfEmpty() {
    Long count = statisticsDailyMetricMapper.countAll();
    if (count != null && count > 0) {
      return;
    }
    LocalDate today = LocalDate.now();
    for (int index = 0; index < DEFAULT_NEW_USERS.size(); index++) {
      StatisticsDailyMetricEntity entity = new StatisticsDailyMetricEntity();
      entity.setMetricDate(today.minusDays(DEFAULT_NEW_USERS.size() - 1L - index));
      entity.setNewUsers(DEFAULT_NEW_USERS.get(index));
      entity.setActiveUsers(DEFAULT_ACTIVE_USERS.get(index));
      entity.setCourseViews(DEFAULT_COURSE_VIEWS.get(index));
      entity.setContentInteractions(DEFAULT_INTERACTIONS.get(index));
      auditFieldHelper.fillForCreate(entity);
      statisticsDailyMetricMapper.insert(entity);
    }
  }

  private void ensureTodayMetric() {
    LocalDate today = LocalDate.now();
    StatisticsDailyMetricEntity todayMetric = statisticsDailyMetricMapper.selectByMetricDate(today);
    if (todayMetric != null) {
      return;
    }
    List<StatisticsDailyMetricEntity> recent = statisticsDailyMetricMapper.selectRecent(DEFAULT_NEW_USERS.size());
    StatisticsDailyMetricEntity latest = recent.isEmpty() ? null : recent.get(0);
    StatisticsDailyMetricEntity created = new StatisticsDailyMetricEntity();
    created.setMetricDate(today);
    created.setNewUsers(resolveNewUsers(latest));
    created.setActiveUsers(resolveActiveUsers(latest));
    created.setCourseViews(resolveCourseViews(latest));
    created.setContentInteractions(resolveContentInteractions(latest));
    auditFieldHelper.fillForCreate(created);
    statisticsDailyMetricMapper.insert(created);
  }

  private int resolveNewUsers(StatisticsDailyMetricEntity latest) {
    long userTotal = safeLong(adminUserMapper.countPage(null, null, null));
    if (latest == null) {
      return Math.max(1, (int) userTotal);
    }
    return Math.max(1, latest.getNewUsers() + Math.toIntExact(userTotal % 4));
  }

  private int resolveActiveUsers(StatisticsDailyMetricEntity latest) {
    long userTotal = safeLong(adminUserMapper.countPage(null, null, null));
    if (latest == null) {
      return Math.max(1, (int) userTotal);
    }
    return Math.max(resolveNewUsers(latest), latest.getActiveUsers() + Math.toIntExact(userTotal % 6));
  }

  private int resolveCourseViews(StatisticsDailyMetricEntity latest) {
    long courseTotal = safeLong(courseMapper.countPage(null, null, null));
    long questionTotal = safeLong(questionMapper.countAll());
    if (latest == null) {
      return Math.max(10, Math.toIntExact(courseTotal * 20 + questionTotal));
    }
    return latest.getCourseViews() + Math.toIntExact(courseTotal * 3 + Math.max(1, questionTotal % 7));
  }

  private int resolveContentInteractions(StatisticsDailyMetricEntity latest) {
    long interactionTotal = gardenContentMapper.selectPage(null, null, 0, 1000).stream()
        .mapToLong(item -> (item.getLikeCount() == null ? 0 : item.getLikeCount()) + (item.getCommentCount() == null ? 0 : item.getCommentCount()))
        .sum();
    if (latest == null) {
      return Math.max(5, Math.toIntExact(interactionTotal / 10));
    }
    return latest.getContentInteractions() + Math.max(1, Math.toIntExact(interactionTotal % 9));
  }

  private long safeLong(Long value) {
    return value == null ? 0L : value;
  }
}
