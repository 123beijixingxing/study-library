import { adaptMemberPackageList, serializeMemberPackagePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import memberPackagesMock from '@/mock/modules/memberPackages';

const { axios, apiHeader, rest } = apiConfig;

export const memberPackageHttp = {
  // 获取会员套餐列表
  getPackageList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? memberPackagesMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/member-packages`, ...rest(object) });
    return applyAdapter(request, adaptMemberPackageList);
  },
  // 保存会员套餐
  savePackage(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeMemberPackagePayload(object.data) };
    return isMockEnabled() ? memberPackagesMock.savePackage(payload) : axios({ method: 'post', url: `${apiHeader}/member-packages`, ...rest(payload) });
  },
  // 删除会员套餐
  deletePackage(packageId: number) {
    return isMockEnabled() ? memberPackagesMock.deletePackage(packageId) : axios({ method: 'delete', url: `${apiHeader}/member-packages/${packageId}` });
  },
};

export default memberPackageHttp;
