import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { CourseEvaluationRecord } from '@/types/admin';

const courseEvaluationsMock = {
  // 模拟获取课程评价列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { courseName?: string; status?: string; keyword?: string };
    const list = db.courseEvaluations.filter(item => {
      const matchCourse = !params.courseName || params.courseName === 'all' || item.courseName === params.courseName;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      const matchKeyword = !params.keyword || [item.courseName, item.username, item.content].some(value => value.includes(params.keyword || ''));
      return matchCourse && matchStatus && matchKeyword;
    });
    return mockSuccess(list);
  },
  // 模拟更新课程评价状态
  updateStatus(evaluationId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as { status: CourseEvaluationRecord['status'] };
    const target = db.courseEvaluations.find(item => item.evaluationId === evaluationId);
    if (target) {
      target.status = data.status;
      writeMockDb(db);
      appendOperationLog('课程评价', '状态变更', `课程评价 ${evaluationId} 状态更新为 ${data.status}`);
    }
    return mockSuccess(target || null);
  },
};

export default courseEvaluationsMock;
