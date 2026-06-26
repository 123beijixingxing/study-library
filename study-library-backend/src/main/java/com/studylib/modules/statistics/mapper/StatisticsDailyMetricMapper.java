package com.studylib.modules.statistics.mapper;

import com.studylib.modules.statistics.entity.StatisticsDailyMetricEntity;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StatisticsDailyMetricMapper {

  Long countAll();

  StatisticsDailyMetricEntity selectLatest();

  List<StatisticsDailyMetricEntity> selectRecent(@Param("limit") Integer limit);

  StatisticsDailyMetricEntity selectByMetricDate(@Param("metricDate") LocalDate metricDate);

  Integer insert(StatisticsDailyMetricEntity entity);

  Integer update(StatisticsDailyMetricEntity entity);
}
