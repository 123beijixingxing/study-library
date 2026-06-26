import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';

const studyRecordsMock = {
  // 模拟获取学习记录列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.studyRecords.filter(item => {
      const matchKeyword = !params.keyword || [item.username, item.courseName].some(value => value.includes(params.keyword || ''));
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟删除学习记录
  deleteRecord(recordId: number) {
    const db = readMockDb();
    const target = db.studyRecords.find(item => item.recordId === recordId);
    db.studyRecords = db.studyRecords.filter(item => item.recordId !== recordId);
    writeMockDb(db);
    if (target) {
      appendOperationLog('学习记录', '删除', `删除学习记录 ${recordId}`);
    }
    return mockSuccess({ success: true });
  },
};

export default studyRecordsMock;
