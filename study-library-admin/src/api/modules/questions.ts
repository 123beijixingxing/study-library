import { adaptQuestion, adaptQuestionList, adaptQuestionVersionList, serializeQuestionPayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import questionsMock from '@/mock/modules/questions';

const { axios, apiHeader, rest } = apiConfig;

export const questionHttp = {
  // 获取题目列表
  getQuestionList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? questionsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/questions`, ...rest(object) });
    return applyAdapter(request, adaptQuestionList);
  },
  // 获取题目详情
  getQuestionDetail(questionId: number) {
    const request = isMockEnabled() ? questionsMock.getDetail(questionId) : axios({ method: 'get', url: `${apiHeader}/questions/${questionId}` });
    return applyAdapter(request, adaptQuestion);
  },
  // 获取题目版本历史
  getQuestionVersionHistory(questionId: number) {
    const request = isMockEnabled()
      ? questionsMock.getVersionHistory(questionId)
      : axios({ method: 'get', url: `${apiHeader}/questions/${questionId}/versions` });
    return applyAdapter(request, adaptQuestionVersionList);
  },
  // 复制题目
  copyQuestion(questionId: number) {
    const request = isMockEnabled() ? questionsMock.copyQuestion(questionId) : axios({ method: 'post', url: `${apiHeader}/questions/${questionId}/copy` });
    return applyAdapter(request, adaptQuestion);
  },
  // 恢复题目
  restoreQuestion(questionId: number) {
    const request = isMockEnabled() ? questionsMock.restoreQuestion(questionId) : axios({ method: 'post', url: `${apiHeader}/questions/${questionId}/restore` });
    return applyAdapter(request, adaptQuestion);
  },
  // 保存题目
  saveQuestion(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeQuestionPayload(object.data) };
    return isMockEnabled() ? questionsMock.saveQuestion(payload) : axios({ method: 'post', url: `${apiHeader}/questions`, ...rest(payload) });
  },
  // 更新题目状态
  updateQuestionStatus(questionId: number, object: Record<string, unknown>) {
    return isMockEnabled()
      ? questionsMock.updateStatus(questionId, object)
      : axios({ method: 'patch', url: `${apiHeader}/questions/${questionId}/status`, ...rest(object) });
  },
  // 导入题目
  importQuestions(object: Record<string, unknown>) {
    return isMockEnabled() ? questionsMock.importQuestions(object) : axios({ method: 'post', url: `${apiHeader}/questions/import`, ...rest(object) });
  },
};

export default questionHttp;
