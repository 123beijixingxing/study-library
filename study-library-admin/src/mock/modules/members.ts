import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { MemberRecord } from '@/types/admin';

const membersMock = {
  // 模拟获取会员列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.members.filter(item => {
      const matchKeyword = !params.keyword || item.username.includes(params.keyword);
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟更新会员信息
  updateMember(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as MemberRecord & { userId?: number };
    const recordId = data.id ?? data.userId;
    const index = db.members.findIndex(item => item.id === recordId);
    if (index !== -1) {
      db.members[index] = {
        ...db.members[index],
        ...data,
      };
      writeMockDb(db);
      appendOperationLog('会员管理', '编辑', `更新会员 ${db.members[index].username}`);
      return mockSuccess(db.members[index]);
    }
    return mockSuccess(null);
  },
};

export default membersMock;
