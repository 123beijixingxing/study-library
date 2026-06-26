import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, readMockDb, writeMockDb } from '@/mock/db';
import type { SensitiveWordRecord } from '@/types/admin';

const sensitiveWordsMock = {
  // 模拟获取敏感词列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.sensitiveWords.filter(item => {
      const matchKeyword = !params.keyword || item.word.includes(params.keyword);
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存敏感词
  saveWord(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as SensitiveWordRecord;
    const index = db.sensitiveWords.findIndex(item => item.wordId === data.wordId);
    if (index !== -1) {
      db.sensitiveWords[index] = {
        ...db.sensitiveWords[index],
        ...data,
        updateTime: formatNow(),
      };
      writeMockDb(db);
      appendOperationLog('敏感词管理', '编辑', `更新敏感词 ${db.sensitiveWords[index].word}`);
      return mockSuccess(db.sensitiveWords[index]);
    }

    const record = {
      ...data,
      wordId: db.sensitiveWords.length ? Math.max(...db.sensitiveWords.map(item => item.wordId)) + 1 : 1,
      updateTime: formatNow(),
    };
    db.sensitiveWords.unshift(record);
    writeMockDb(db);
    appendOperationLog('敏感词管理', '新增', `创建敏感词 ${record.word}`);
    return mockSuccess(record);
  },
  // 模拟删除敏感词
  deleteWord(wordId: number) {
    const db = readMockDb();
    const index = db.sensitiveWords.findIndex(item => item.wordId === wordId);
    if (index !== -1) {
      const [removed] = db.sensitiveWords.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('敏感词管理', '删除', `删除敏感词 ${removed.word}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default sensitiveWordsMock;
