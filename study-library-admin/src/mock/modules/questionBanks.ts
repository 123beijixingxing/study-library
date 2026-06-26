import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, nextId, readMockDb, writeMockDb } from '@/mock/db';
import type { QuestionBankRecord } from '@/types/admin';

const questionBankMock = {
  // 模拟获取题库列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.questionBanks.filter(item => {
      const matchKeyword = !params.keyword || item.bankName.includes(params.keyword) || item.categoryName.includes(params.keyword);
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存题库
  saveQuestionBank(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as QuestionBankRecord & { bankId?: number };
    const recordId = data.id ?? data.bankId;
    if (recordId) {
      const index = db.questionBanks.findIndex(item => item.id === recordId);
      if (index !== -1) {
        db.questionBanks[index] = {
          ...db.questionBanks[index],
          ...data,
          id: recordId,
          updateTime: formatNow(),
        };
        writeMockDb(db);
        appendOperationLog('题库管理', '编辑', `更新题库 ${db.questionBanks[index].bankName}`);
        return mockSuccess(db.questionBanks[index]);
      }
    }

    const record: QuestionBankRecord = {
      ...data,
      id: recordId || nextId(db.questionBanks),
      updateTime: formatNow(),
    };
    db.questionBanks.unshift(record);
    writeMockDb(db);
    appendOperationLog('题库管理', '新增', `创建题库 ${record.bankName}`);
    return mockSuccess(record);
  },
  // 模拟删除题库
  deleteQuestionBank(id: number) {
    const db = readMockDb();
    const index = db.questionBanks.findIndex(item => item.id === id);
    if (index !== -1) {
      const [removed] = db.questionBanks.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('题库管理', '删除', `删除题库 ${removed.bankName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default questionBankMock;
