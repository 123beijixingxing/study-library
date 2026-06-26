import { adaptReportList, serializeReportHandlePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import reportsMock from '@/mock/modules/reports';

const { axios, apiHeader, rest } = apiConfig;

export const reportHttp = {
  // 获取举报列表
  getReportList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? reportsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/reports`, ...rest(object) });
    return applyAdapter(request, adaptReportList);
  },
  // 处理举报
  handleReport(reportId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeReportHandlePayload(object.data) };
    return isMockEnabled() ? reportsMock.handleReport(reportId, payload) : axios({ method: 'post', url: `${apiHeader}/reports/${reportId}/handle`, ...rest(payload) });
  },
};

export default reportHttp;
