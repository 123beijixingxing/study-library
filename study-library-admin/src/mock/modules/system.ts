import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { SystemFeatureRecord, SystemInfoRecord } from '@/types/admin';

const systemMock = {
  // 模拟获取系统信息
  getSystemInfo() {
    const db = readMockDb();
    return mockSuccess(db.systemInfo);
  },
  // 模拟更新系统信息
  updateSystemInfo(payload: Record<string, unknown>) {
    const db = readMockDb();
    db.systemInfo = {
      ...db.systemInfo,
      ...(payload.data as Partial<SystemInfoRecord>),
    };
    writeMockDb(db);
    appendOperationLog('系统管理', '编辑', '更新系统信息');
    return mockSuccess(db.systemInfo);
  },
  // 模拟获取功能开关列表
  getFeatureList() {
    const db = readMockDb();
    return mockSuccess(db.systemFeatures);
  },
  // 模拟更新功能开关
  updateFeature(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as Partial<SystemFeatureRecord> & { code: string };
    const index = db.systemFeatures.findIndex(item => item.code === data.code);
    if (index !== -1) {
      db.systemFeatures[index] = {
        ...db.systemFeatures[index],
        ...data,
      };
      writeMockDb(db);
      appendOperationLog('系统管理', '状态变更', `更新功能开关 ${db.systemFeatures[index].name}`);
      return mockSuccess(db.systemFeatures[index]);
    }
    return mockSuccess(null);
  },
};

export default systemMock;
