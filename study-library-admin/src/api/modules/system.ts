import { adaptSystemFeatureList, adaptSystemInfo, serializeSystemFeaturePayload, serializeSystemInfoPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import systemMock from '@/mock/modules/system';

const { axios, apiHeader, rest } = apiConfig;

export const systemHttp = {
  // 获取系统信息
  getSystemInfo(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? systemMock.getSystemInfo() : axios({ method: 'get', url: `${apiHeader}/system/info`, ...rest(object) });
    return applyAdapter(request, adaptSystemInfo);
  },
  // 更新系统信息
  updateSystemInfo(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeSystemInfoPayload(object.data) };
    return isMockEnabled() ? systemMock.updateSystemInfo(payload) : axios({ method: 'put', url: `${apiHeader}/system/info`, ...rest(payload) });
  },
  // 获取功能开关列表
  getFeatureList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? systemMock.getFeatureList() : axios({ method: 'get', url: `${apiHeader}/system/features`, ...rest(object) });
    return applyAdapter(request, adaptSystemFeatureList);
  },
  // 更新功能开关
  updateFeature(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeSystemFeaturePayload(object.data) };
    return isMockEnabled() ? systemMock.updateFeature(payload) : axios({ method: 'put', url: `${apiHeader}/system/features`, ...rest(payload) });
  },
};

export default systemHttp;
