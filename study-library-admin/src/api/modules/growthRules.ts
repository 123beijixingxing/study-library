import { adaptGrowthRuleList, serializeGrowthRulePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import growthRulesMock from '@/mock/modules/growthRules';

const { axios, apiHeader, rest } = apiConfig;

export const growthRuleHttp = {
  // 获取成长规则列表
  getRuleList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? growthRulesMock.getList() : axios({ method: 'get', url: `${apiHeader}/growth-rules`, ...rest(object) });
    return applyAdapter(request, adaptGrowthRuleList);
  },
  // 保存成长规则
  saveRule(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeGrowthRulePayload(object.data) };
    return isMockEnabled() ? growthRulesMock.saveRule(payload) : axios({ method: 'put', url: `${apiHeader}/growth-rules`, ...rest(payload) });
  },
};

export default growthRuleHttp;
