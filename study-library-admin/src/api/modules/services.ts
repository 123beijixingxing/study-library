import { adaptServiceList, serializeServiceReplyPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import servicesMock from '@/mock/modules/services';

const { axios, apiHeader, rest } = apiConfig;

export const serviceHttp = {
  // 获取客服工单列表
  getTicketList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? servicesMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/service-tickets`, ...rest(object) });
    return applyAdapter(request, adaptServiceList);
  },
  // 回复客服工单
  replyTicket(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeServiceReplyPayload(object.data) };
    return isMockEnabled() ? servicesMock.replyTicket(payload) : axios({ method: 'post', url: `${apiHeader}/service-tickets/reply`, ...rest(payload) });
  },
  // 删除客服工单
  deleteTicket(id: number) {
    return isMockEnabled() ? servicesMock.deleteTicket(id) : axios({ method: 'delete', url: `${apiHeader}/service-tickets/${id}` });
  },
};

export default serviceHttp;
