<template>
  <div class="page-shell">
    <section class="overview-grid" v-loading="loading">
      <OverviewCard v-for="item in cards" :key="item.title" :title="item.title" :value="item.value" :description="item.description" />
    </section>

    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">数据统计</h1>
          <p class="page-desc">展示真实统计表中的用户增长、活跃度、课程访问和互动趋势，支持后续扩展图表组件。</p>
        </div>
        <el-button type="primary" @click="fetchStatistics">刷新统计</el-button>
      </div>

      <el-table :data="trendList" v-loading="loading" border class="table-block">
        <el-table-column prop="date" label="统计日期" width="120" />
        <el-table-column prop="newUsers" label="新增用户" width="120" />
        <el-table-column prop="activeUsers" label="活跃用户" width="120" />
        <el-table-column prop="courseViews" label="课程访问量" width="140" />
        <el-table-column prop="contentInteractions" label="内容互动量" width="140" />
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { statisticsHttp } from '@/api/modules/statistics';
import OverviewCard from '@/components/business/OverviewCard.vue';
import type { StatisticsOverviewRecord, StatisticsTrendRecord } from '@/types/admin';

const loading = ref(false);
const overview = ref<StatisticsOverviewRecord>({
  totalUsers: 0,
  totalCourses: 0,
  totalQuestions: 0,
  totalInteractions: 0,
});
const trendList = ref<StatisticsTrendRecord[]>([]);

const cards = computed(() => [
  { title: '用户总量', value: `${overview.value.totalUsers}`, description: '注册用户总规模' },
  { title: '课程总量', value: `${overview.value.totalCourses}`, description: '当前后台维护课程数量' },
  { title: '题目总量', value: `${overview.value.totalQuestions}`, description: '题库累计题目数量' },
  { title: '互动总量', value: `${overview.value.totalInteractions}`, description: '园地点赞评论等互动数据' },
]);

const fetchStatistics = async () => {
  loading.value = true;
  try {
    const [overviewRes, trendRes] = await Promise.all([statisticsHttp.getOverview(), statisticsHttp.getTrends()]);
    overview.value = overviewRes.data;
    trendList.value = trendRes.data;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchStatistics);
</script>

<style scoped lang="scss">
.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

.toolbar-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.table-block {
  margin-top: 18px;
}

@media (max-width: 960px) {
  .toolbar-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
