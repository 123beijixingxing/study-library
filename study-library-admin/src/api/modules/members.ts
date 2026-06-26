import { adaptMemberList, serializeMemberPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import membersMock from '@/mock/modules/members';

const { axios, apiHeader, rest } = apiConfig;

export const memberHttp = {
  // 获取会员列表
  getMemberList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? membersMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/members`, ...rest(object) });
    return applyAdapter(request, adaptMemberList);
  },
  // 更新会员信息
  updateMember(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeMemberPayload(object.data) };
    return isMockEnabled() ? membersMock.updateMember(payload) : axios({ method: 'put', url: `${apiHeader}/members`, ...rest(payload) });
  },
};

export default memberHttp;
