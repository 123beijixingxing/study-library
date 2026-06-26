import { adaptDashboardSummary, adaptDashboardTrend } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import dashboardMock from '@/mock/modules/dashboard';

const { axios, apiHeader, rest } = apiConfig;

export const dashboardHttp = {
  // 获取首页概览数据
  getSummary(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? dashboardMock.getSummary() : axios({ method: 'get', url: `${apiHeader}/dashboard/summary`, ...rest(object) });
    return applyAdapter(request, adaptDashboardSummary);
  },
  // 获取用户趋势数据
  getUserTrend(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? dashboardMock.getUserTrend() : axios({ method: 'get', url: `${apiHeader}/dashboard/user-trend`, ...rest(object) });
    return applyAdapter(request, adaptDashboardTrend);
  },
};

export default dashboardHttp;
