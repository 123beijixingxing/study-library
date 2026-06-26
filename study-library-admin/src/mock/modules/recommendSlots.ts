import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { RecommendSlotRecord } from '@/types/admin';

const recommendSlotsMock = {
  // 模拟获取推荐位列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { pageCode?: string; status?: string };
    const list = db.recommendSlots.filter(item => {
      const matchPageCode = !params.pageCode || params.pageCode === 'all' || item.pageCode === params.pageCode;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchPageCode && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存推荐位
  saveSlot(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as RecommendSlotRecord;
    const index = db.recommendSlots.findIndex(item => item.slotId === data.slotId);
    if (index !== -1) {
      db.recommendSlots[index] = { ...db.recommendSlots[index], ...data };
      writeMockDb(db);
      appendOperationLog('推荐位配置', '编辑', `更新推荐位 ${db.recommendSlots[index].slotName}`);
      return mockSuccess(db.recommendSlots[index]);
    }

    const record = {
      ...data,
      slotId: db.recommendSlots.length ? Math.max(...db.recommendSlots.map(item => item.slotId)) + 1 : 1,
    };
    db.recommendSlots.unshift(record);
    writeMockDb(db);
    appendOperationLog('推荐位配置', '新增', `创建推荐位 ${record.slotName}`);
    return mockSuccess(record);
  },
  // 模拟删除推荐位
  deleteSlot(slotId: number) {
    const db = readMockDb();
    const index = db.recommendSlots.findIndex(item => item.slotId === slotId);
    if (index !== -1) {
      const [removed] = db.recommendSlots.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('推荐位配置', '删除', `删除推荐位 ${removed.slotName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default recommendSlotsMock;
