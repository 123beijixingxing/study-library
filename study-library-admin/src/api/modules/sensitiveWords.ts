import { adaptSensitiveWordList, serializeSensitiveWordPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import sensitiveWordsMock from '@/mock/modules/sensitiveWords';

const { axios, apiHeader, rest } = apiConfig;

export const sensitiveWordHttp = {
  // 获取敏感词列表
  getWordList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? sensitiveWordsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/sensitive-words`, ...rest(object) });
    return applyAdapter(request, adaptSensitiveWordList);
  },
  // 保存敏感词
  saveWord(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeSensitiveWordPayload(object.data) };
    return isMockEnabled() ? sensitiveWordsMock.saveWord(payload) : axios({ method: 'post', url: `${apiHeader}/sensitive-words`, ...rest(payload) });
  },
  // 删除敏感词
  deleteWord(wordId: number) {
    return isMockEnabled() ? sensitiveWordsMock.deleteWord(wordId) : axios({ method: 'delete', url: `${apiHeader}/sensitive-words/${wordId}` });
  },
};

export default sensitiveWordHttp;
