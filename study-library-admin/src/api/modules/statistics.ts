import { adaptStatisticsOverview, adaptStatisticsTrendList } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import statisticsMock from '@/mock/modules/statistics';

const { axios, apiHeader, rest } = apiConfig;

export const statisticsHttp = {
  // 获取统计概览
  getOverview(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? statisticsMock.getOverview() : axios({ method: 'get', url: `${apiHeader}/statistics/overview`, ...rest(object) });
    return applyAdapter(request, adaptStatisticsOverview);
  },
  // 获取统计趋势
  getTrends(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? statisticsMock.getTrends() : axios({ method: 'get', url: `${apiHeader}/statistics/trends`, ...rest(object) });
    return applyAdapter(request, adaptStatisticsTrendList);
  },
};

export default statisticsHttp;
