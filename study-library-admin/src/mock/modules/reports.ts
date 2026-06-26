import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { ReportRecord } from '@/types/admin';

const reportsMock = {
  // 模拟获取举报列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { reportType?: string; status?: string; keyword?: string };
    const list = db.reports.filter(item => {
      const matchType = !params.reportType || params.reportType === 'all' || item.reportType === params.reportType;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      const matchKeyword = !params.keyword || [item.targetTitle, item.reporterName].some(value => value.includes(params.keyword || ''));
      return matchType && matchStatus && matchKeyword;
    });
    return mockSuccess(list);
  },
  // 模拟处理举报
  handleReport(reportId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<ReportRecord> & { handleResult?: string; handleRemark?: string; status?: ReportRecord['status'] };
    const target = db.reports.find(item => item.reportId === reportId);
    if (target) {
      target.status = data.status || target.status;
      target.handleResult = data.handleResult || target.handleResult;
      target.handleRemark = data.handleRemark || target.handleRemark;
      writeMockDb(db);
      appendOperationLog('举报审核', '编辑', `处理举报 ${reportId}`);
    }
    return mockSuccess(target || null);
  },
};

export default reportsMock;
