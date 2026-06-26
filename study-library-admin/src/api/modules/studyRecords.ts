import apiConfig from '@/api/utils/apiConfig';
import { adaptStudyRecordList } from '@/api/adapters/admin';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import studyRecordsMock from '@/mock/modules/studyRecords';

const { axios, apiHeader, rest } = apiConfig;

export const studyRecordHttp = {
  // 获取学习记录列表
  getStudyRecordList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? studyRecordsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/study-records`, ...rest(object) });
    return applyAdapter(request, adaptStudyRecordList);
  },
  // 删除学习记录
  deleteStudyRecord(recordId: number) {
    return isMockEnabled() ? studyRecordsMock.deleteRecord(recordId) : axios({ method: 'delete', url: `${apiHeader}/study-records/${recordId}` });
  },
};

export default studyRecordHttp;
