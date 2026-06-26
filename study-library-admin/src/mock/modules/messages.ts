import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, nextId, readMockDb, writeMockDb } from '@/mock/db';
import type { MessageRecord } from '@/types/admin';

const messagesMock = {
  // 模拟获取消息列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; sendStatus?: string };
    const list = db.messages.filter(item => {
      const matchKeyword = !params.keyword || item.title.includes(params.keyword);
      const matchStatus = !params.sendStatus || params.sendStatus === 'all' || item.sendStatus === params.sendStatus;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存消息
  saveMessage(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as MessageRecord & { messageId?: number };
    const recordId = data.id ?? data.messageId;
    if (recordId) {
      const index = db.messages.findIndex(item => item.id === recordId);
      if (index !== -1) {
        db.messages[index] = {
          ...db.messages[index],
          ...data,
          id: recordId,
        };
        writeMockDb(db);
        appendOperationLog('消息管理', '编辑', `更新消息 ${db.messages[index].title}`);
        return mockSuccess(db.messages[index]);
      }
    }

    const record: MessageRecord = {
      ...data,
      id: recordId || nextId(db.messages),
      sendTime: data.sendTime || formatNow(),
    };
    db.messages.unshift(record);
    writeMockDb(db);
    appendOperationLog('消息管理', '新增', `创建消息 ${record.title}`);
    return mockSuccess(record);
  },
  // 模拟删除消息
  deleteMessage(id: number) {
    const db = readMockDb();
    const index = db.messages.findIndex(item => item.id === id);
    if (index !== -1) {
      const [removed] = db.messages.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('消息管理', '删除', `删除消息 ${removed.title}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default messagesMock;
