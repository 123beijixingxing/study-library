import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { GardenRecord } from '@/types/admin';

const gardenMock = {
  // 模拟获取园地内容列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { contentType?: GardenRecord['contentType']; auditStatus?: string };
    const list = db.gardenContents.filter(item => {
      const matchType = !params.contentType || item.contentType === params.contentType;
      const matchStatus = !params.auditStatus || params.auditStatus === 'all' || item.auditStatus === params.auditStatus;
      return matchType && matchStatus;
    });

    return mockSuccess(list);
  },
  // 模拟审核园地内容
  auditContent(contentId: number, payload: Record<string, unknown>) {
    const db = readMockDb();
    const auditStatus = (payload.data as { auditStatus: GardenRecord['auditStatus'] }).auditStatus;
    const target = db.gardenContents.find(item => item.id === contentId);
    if (target) {
      target.auditStatus = auditStatus;
      writeMockDb(db);
      appendOperationLog('园地管理', '状态变更', `内容 ${contentId} 审核状态改为 ${auditStatus}`);
    }
    return mockSuccess(target || null);
  },
  // 模拟删除园地内容
  deleteContent(contentId: number) {
    const db = readMockDb();
    const index = db.gardenContents.findIndex(item => item.id === contentId);
    if (index !== -1) {
      const [removed] = db.gardenContents.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('园地管理', '删除', `删除内容 ${removed.sourceName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default gardenMock;
