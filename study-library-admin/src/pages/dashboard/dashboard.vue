<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">后台仪表盘</h1>
          <p class="page-desc">用于承接学习通后台的核心运营概览，包括用户、课程、内容互动、会员和系统运行情况。</p>
        </div>
        <el-button type="primary" @click="fetchDashboard">刷新概览</el-button>
      </div>
    </section>

    <section class="overview-grid" v-loading="loading">
      <OverviewCard v-for="item in cards" :key="item.title" :title="item.title" :value="item.value" :description="item.description" />
    </section>

    <section class="page-card quick-panel">
        <div>
          <h2 class="section-title">近 7 次用户增长趋势</h2>
          <p class="page-desc">当前展示真实后台统计接口返回的近 7 日新增用户趋势，后续可升级为图表视图。</p>
        </div>
      <div class="trend-list">
        <div v-for="item in trendList" :key="item.date" class="trend-item">
          <span>{{ item.date }}</span>
          <strong>{{ item.value }}</strong>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import OverviewCard from '@/components/business/OverviewCard.vue';
import { dashboardHttp } from '@/api/modules/dashboard';
import type { DashboardSummary, DashboardTrendPoint } from '@/types/admin';

const loading = ref(false);
const summary = ref<DashboardSummary>({
  userTotal: 0,
  courseTotal: 0,
  activeToday: 0,
  memberTotal: 0,
});
const trendList = ref<DashboardTrendPoint[]>([]);

const cards = computed(() => [
  { title: '用户规模', value: `${summary.value.userTotal}`, description: '累计注册用户与会员增长趋势入口' },
  { title: '课程内容', value: `${summary.value.courseTotal}`, description: '课程、章节、分类与推荐位管理概览' },
  { title: '今日活跃', value: `${summary.value.activeToday}`, description: '当日访问和活跃用户概览' },
  { title: '会员总量', value: `${summary.value.memberTotal}`, description: '会员体系、套餐与续费运营数据' },
]);

const fetchDashboard = async () => {
  loading.value = true;
  try {
    const [summaryRes, trendRes] = await Promise.all([dashboardHttp.getSummary(), dashboardHttp.getUserTrend()]);
    summary.value = summaryRes.data;
    trendList.value = trendRes.data;
  } finally {
    loading.value = false;
  }
};

onMounted(fetchDashboard);
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

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.quick-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.trend-list {
  min-width: 320px;
  display: grid;
  gap: 10px;
}

.trend-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 14px;
  border-radius: 12px;
  background: var(--admin-primary-soft);
  color: var(--admin-primary);
}

@media (max-width: 900px) {
  .toolbar-head,
  .quick-panel {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
