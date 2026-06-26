import { adaptHomeSectionList, serializeHomeSectionPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import homeMock from '@/mock/modules/home';

const { axios, apiHeader, rest } = apiConfig;

export const homeManageHttp = {
  // 获取首页配置列表
  getSectionList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? homeMock.getSectionList(object) : axios({ method: 'get', url: `${apiHeader}/home/courses`, ...rest(object) });
    return applyAdapter(request, adaptHomeSectionList);
  },
  // 更新首页配置
  updateSection(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeHomeSectionPayload(object.data) };
    return isMockEnabled() ? homeMock.updateSection(payload) : axios({ method: 'put', url: `${apiHeader}/home/courses`, ...rest(payload) });
  },
  // 删除首页配置
  deleteSection(id: number) {
    return isMockEnabled() ? homeMock.deleteSection(id) : axios({ method: 'delete', url: `${apiHeader}/home/courses/${id}` });
  },
};

export default homeManageHttp;
