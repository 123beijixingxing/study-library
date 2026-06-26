import { mockSuccess } from '@/mock';
import { readMockDb } from '@/mock/db';

const logsMock = {
  // 模拟获取操作日志列表
  getOperationLogs(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; operationType?: string };
    const list = db.operationLogs.filter(item => {
      const matchKeyword = !params.keyword || [item.operatorName, item.operationModule, item.operationContent].some(value => value.includes(params.keyword || ''));
      const matchType = !params.operationType || params.operationType === 'all' || item.operationType === params.operationType;
      return matchKeyword && matchType;
    });
    return mockSuccess(list);
  },
  // 模拟获取登录日志列表
  getLoginLogs(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.loginLogs.filter(item => {
      const matchKeyword = !params.keyword || item.username.includes(params.keyword || '');
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
};

export default logsMock;
