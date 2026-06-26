import { mockSuccess } from '@/mock';
import { readMockDb } from '@/mock/db';
import type { DashboardSummary, DashboardTrendPoint } from '@/types/admin';

const dashboardMock = {
  getSummary() {
    const db = readMockDb();
    const latestTrend = db.statisticsTrends[db.statisticsTrends.length - 1];
    const summary: DashboardSummary = {
      userTotal: db.users.length,
      courseTotal: db.courses.length,
      activeToday: latestTrend?.activeUsers || 0,
      memberTotal: db.members.filter(item => item.status === 'active' || item.status === 'expiring').length,
    };
    return mockSuccess(summary);
  },
  getUserTrend() {
    const db = readMockDb();
    const trend: DashboardTrendPoint[] = db.statisticsTrends.map(item => ({
      date: item.date,
      value: item.newUsers,
    }));
    return mockSuccess(trend);
  },
};

export default dashboardMock;
