import apiConfig from '@/api/utils/apiConfig';
import { adaptCourseEvaluationList, serializeCourseEvaluationPayload } from '@/api/adapters/admin';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import courseEvaluationsMock from '@/mock/modules/courseEvaluations';

const { axios, apiHeader, rest } = apiConfig;

export const courseEvaluationHttp = {
  // 获取课程评价列表
  getEvaluationList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? courseEvaluationsMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/course-evaluations`, ...rest(object) });
    return applyAdapter(request, adaptCourseEvaluationList);
  },
  // 更新课程评价状态
  updateEvaluationStatus(evaluationId: number, object: Record<string, unknown>) {
    const payload = { ...object, data: serializeCourseEvaluationPayload(object.data) };
    return isMockEnabled()
      ? courseEvaluationsMock.updateStatus(evaluationId, payload)
      : axios({ method: 'patch', url: `${apiHeader}/course-evaluations/${evaluationId}/status`, ...rest(payload) });
  },
};

export default courseEvaluationHttp;
