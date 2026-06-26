import { adaptQuestionBankList, serializeQuestionBankPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import questionBankMock from '@/mock/modules/questionBanks';

const { axios, apiHeader, rest } = apiConfig;

export const questionBankHttp = {
  // 获取题库列表
  getQuestionBankList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? questionBankMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/question-banks`, ...rest(object) });
    return applyAdapter(request, adaptQuestionBankList);
  },
  // 保存题库
  saveQuestionBank(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeQuestionBankPayload(object.data) };
    return isMockEnabled() ? questionBankMock.saveQuestionBank(payload) : axios({ method: 'post', url: `${apiHeader}/question-banks`, ...rest(payload) });
  },
  // 删除题库
  deleteQuestionBank(id: number) {
    return isMockEnabled() ? questionBankMock.deleteQuestionBank(id) : axios({ method: 'delete', url: `${apiHeader}/question-banks/${id}` });
  },
};

export default questionBankHttp;
