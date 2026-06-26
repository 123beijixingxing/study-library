import { adaptLoginLogList, adaptOperationLogList } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import logsMock from '@/mock/modules/logs';

const { axios, apiHeader, rest } = apiConfig;

export const logsHttp = {
  // 获取操作日志列表
  getOperationLogs(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? logsMock.getOperationLogs(object) : axios({ method: 'get', url: `${apiHeader}/logs/operations`, ...rest(object) });
    return applyAdapter(request, adaptOperationLogList);
  },
  // 获取登录日志列表
  getLoginLogs(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? logsMock.getLoginLogs(object) : axios({ method: 'get', url: `${apiHeader}/logs/logins`, ...rest(object) });
    return applyAdapter(request, adaptLoginLogList);
  },
};

export default logsHttp;
