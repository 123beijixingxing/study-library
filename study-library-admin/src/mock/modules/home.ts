import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { HomeSectionRecord } from '@/types/admin';

const homeMock = {
  // 模拟获取首页配置列表
  getSectionList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { sectionType?: HomeSectionRecord['sectionType'] };
    const list = params.sectionType ? db.homeSections.filter(item => item.sectionType === params.sectionType) : db.homeSections;
    return mockSuccess(list);
  },
  // 模拟更新首页配置
  updateSection(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<HomeSectionRecord> & { id?: number; configId?: number };
    const recordId = data.id ?? data.configId;
    const index = db.homeSections.findIndex(item => item.id === recordId);
    if (index !== -1) {
      db.homeSections[index] = {
        ...db.homeSections[index],
        ...data,
      };
      writeMockDb(db);
      appendOperationLog('首页管理', '编辑', `更新首页配置 ${db.homeSections[index].title}`);
      return mockSuccess(db.homeSections[index]);
    }
    return mockSuccess(null);
  },
  // 模拟删除首页配置
  deleteSection(id: number) {
    const db = readMockDb();
    const index = db.homeSections.findIndex(item => item.id === id);
    if (index !== -1) {
      const [removed] = db.homeSections.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('首页管理', '删除', `删除首页配置 ${removed.title}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default homeMock;
