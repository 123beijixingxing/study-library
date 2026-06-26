import { mockSuccess } from '@/mock';
import { readMockDb } from '@/mock/db';
import type { StatisticsOverviewRecord } from '@/types/admin';

const statisticsMock = {
  // 模拟获取统计概览
  getOverview() {
    const db = readMockDb();
    const totalInteractions = db.gardenContents.reduce((sum, item) => sum + item.likeCount + item.commentCount, 0);
    const overview: StatisticsOverviewRecord = {
      totalUsers: db.users.length,
      totalCourses: db.courses.length,
      totalQuestions: db.questionBanks.reduce((sum, item) => sum + item.questionCount, 0),
      totalInteractions,
    };
    return mockSuccess(overview);
  },
  // 模拟获取统计趋势
  getTrends() {
    const db = readMockDb();
    return mockSuccess(db.statisticsTrends);
  },
};

export default statisticsMock;
