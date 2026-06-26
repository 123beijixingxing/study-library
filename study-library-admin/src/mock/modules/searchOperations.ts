import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { SearchOperationRecord } from '@/types/admin';

const searchOperationsMock = {
  // 模拟获取热搜词列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; scene?: string; status?: string };
    const list = db.searchOperations.filter(item => {
      const matchKeyword = !params.keyword || item.keyword.includes(params.keyword);
      const matchScene = !params.scene || params.scene === 'all' || item.scene === params.scene;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchScene && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存热搜词
  saveKeyword(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as SearchOperationRecord;
    const index = db.searchOperations.findIndex(item => item.keywordId === data.keywordId);
    if (index !== -1) {
      db.searchOperations[index] = { ...db.searchOperations[index], ...data };
      writeMockDb(db);
      appendOperationLog('搜索运营', '编辑', `更新热搜词 ${db.searchOperations[index].keyword}`);
      return mockSuccess(db.searchOperations[index]);
    }

    const record = {
      ...data,
      keywordId: db.searchOperations.length ? Math.max(...db.searchOperations.map(item => item.keywordId)) + 1 : 1,
    };
    db.searchOperations.unshift(record);
    writeMockDb(db);
    appendOperationLog('搜索运营', '新增', `创建热搜词 ${record.keyword}`);
    return mockSuccess(record);
  },
  // 模拟删除热搜词
  deleteKeyword(keywordId: number) {
    const db = readMockDb();
    const index = db.searchOperations.findIndex(item => item.keywordId === keywordId);
    if (index !== -1) {
      const [removed] = db.searchOperations.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('搜索运营', '删除', `删除热搜词 ${removed.keyword}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default searchOperationsMock;
