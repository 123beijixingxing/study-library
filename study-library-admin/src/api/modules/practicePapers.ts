import { adaptPracticeAnalysis, adaptPracticePaperDetail, adaptPracticePaperList, adaptPracticePeerList, adaptWrongQuestionList, serializePracticePaperDetailPayload, serializePracticePaperPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import practicePapersMock from '@/mock/modules/practicePapers';

const { axios, apiHeader, rest } = apiConfig;

export const practicePaperHttp = {
  // 获取试卷列表
  getPaperList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? practicePapersMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/practice-papers`, ...rest(object) });
    return applyAdapter(request, adaptPracticePaperList);
  },
  // 保存试卷
  savePaper(object: Record<string, unknown>) {
    const payload = { ...object, data: serializePracticePaperPayload(object.data) };
    return isMockEnabled() ? practicePapersMock.savePaper(payload) : axios({ method: 'post', url: `${apiHeader}/practice-papers`, ...rest(payload) });
  },
  // 删除试卷
  deletePaper(paperId: number) {
    return isMockEnabled() ? practicePapersMock.deletePaper(paperId) : axios({ method: 'delete', url: `${apiHeader}/practice-papers/${paperId}` });
  },
  // 获取试卷详情
  getPaperDetail(paperId: number) {
    const request = isMockEnabled() ? practicePapersMock.getDetail(paperId) : axios({ method: 'get', url: `${apiHeader}/practice-papers/${paperId}` });
    return applyAdapter(request, adaptPracticePaperDetail);
  },
  // 保存试卷详情
  savePaperDetail(paperId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializePracticePaperDetailPayload(object.data) };
    return isMockEnabled()
      ? practicePapersMock.saveDetail(paperId, payload)
      : axios({ method: 'put', url: `${apiHeader}/practice-papers/${paperId}`, ...rest(payload) });
  },
  // 获取试卷分析
  getPracticeAnalysis(paperId: number, object: Record<string, unknown> = {}) {
    const request = isMockEnabled()
      ? practicePapersMock.getAnalysis(paperId)
      : axios({ method: 'get', url: `${apiHeader}/practice-records/analysis`, ...rest(object) });
    return applyAdapter(request, adaptPracticeAnalysis);
  },
  // 获取错题分析
  getWrongQuestionAnalysis(paperId: number, object: Record<string, unknown> = {}) {
    const request = isMockEnabled()
      ? practicePapersMock.getWrongQuestions(paperId)
      : axios({ method: 'get', url: `${apiHeader}/wrong-questions/analysis`, ...rest(object) });
    return applyAdapter(request, adaptWrongQuestionList);
  },
  // 获取同群体对比
  getPeerComparison(paperId: number, object: Record<string, unknown> = {}) {
    const request = isMockEnabled()
      ? practicePapersMock.getPeerComparison(paperId)
      : axios({ method: 'get', url: `${apiHeader}/practice-papers/${paperId}/peer-comparison`, ...rest(object) });
    return applyAdapter(request, adaptPracticePeerList);
  },
};

export default practicePaperHttp;
