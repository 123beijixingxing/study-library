import { adaptSearchOperationList, serializeSearchOperationPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import searchOperationsMock from '@/mock/modules/searchOperations';

const { axios, apiHeader, rest } = apiConfig;

export const searchOperationHttp = {
  // 获取热搜词列表
  getKeywordList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? searchOperationsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/search/hot-keywords`, ...rest(object) });
    return applyAdapter(request, adaptSearchOperationList);
  },
  // 保存热搜词
  saveKeyword(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeSearchOperationPayload(object.data) };
    return isMockEnabled() ? searchOperationsMock.saveKeyword(payload) : axios({ method: 'post', url: `${apiHeader}/search/hot-keywords`, ...rest(payload) });
  },
  // 删除热搜词
  deleteKeyword(keywordId: number) {
    return isMockEnabled() ? searchOperationsMock.deleteKeyword(keywordId) : axios({ method: 'delete', url: `${apiHeader}/search/hot-keywords/${keywordId}` });
  },
};

export default searchOperationHttp;
