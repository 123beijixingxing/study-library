import { adaptMessageList, serializeMessagePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import messagesMock from '@/mock/modules/messages';

const { axios, apiHeader, rest } = apiConfig;

export const messageHttp = {
  // 获取消息列表
  getMessageList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? messagesMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/messages`, ...rest(object) });
    return applyAdapter(request, adaptMessageList);
  },
  // 保存消息
  saveMessage(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeMessagePayload(object.data) };
    return isMockEnabled() ? messagesMock.saveMessage(payload) : axios({ method: 'post', url: `${apiHeader}/messages`, ...rest(payload) });
  },
  // 删除消息
  deleteMessage(id: number) {
    return isMockEnabled() ? messagesMock.deleteMessage(id) : axios({ method: 'delete', url: `${apiHeader}/messages/${id}` });
  },
};

export default messageHttp;
