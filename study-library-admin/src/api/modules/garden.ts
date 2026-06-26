import { adaptGardenList, serializeGardenAuditPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import gardenMock from '@/mock/modules/garden';

const { axios, apiHeader, rest } = apiConfig;

export const gardenHttp = {
  // 获取园地内容列表
  getContentList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? gardenMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/contents`, ...rest(object) });
    return applyAdapter(request, adaptGardenList);
  },
  // 审核园地内容
  auditContent(contentId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeGardenAuditPayload(object.data) };
    return isMockEnabled() ? gardenMock.auditContent(contentId, payload) : axios({ method: 'post', url: `${apiHeader}/contents/${contentId}/audit`, ...rest(payload) });
  },
  // 删除园地内容
  deleteContent(contentId: number) {
    return isMockEnabled() ? gardenMock.deleteContent(contentId) : axios({ method: 'delete', url: `${apiHeader}/contents/${contentId}` });
  },
};

export default gardenHttp;
