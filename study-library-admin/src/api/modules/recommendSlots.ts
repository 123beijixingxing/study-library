import { adaptRecommendSlotList, serializeRecommendSlotPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import recommendSlotsMock from '@/mock/modules/recommendSlots';

const { axios, apiHeader, rest } = apiConfig;

export const recommendSlotHttp = {
  // 获取推荐位列表
  getSlotList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? recommendSlotsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/recommend-slots`, ...rest(object) });
    return applyAdapter(request, adaptRecommendSlotList);
  },
  // 保存推荐位
  saveSlot(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeRecommendSlotPayload(object.data) };
    return isMockEnabled() ? recommendSlotsMock.saveSlot(payload) : axios({ method: 'post', url: `${apiHeader}/recommend-slots`, ...rest(payload) });
  },
  // 删除推荐位
  deleteSlot(slotId: number) {
    return isMockEnabled() ? recommendSlotsMock.deleteSlot(slotId) : axios({ method: 'delete', url: `${apiHeader}/recommend-slots/${slotId}` });
  },
};

export default recommendSlotHttp;
