import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { MemberPackageRecord } from '@/types/admin';

const memberPackagesMock = {
  // 模拟获取会员套餐列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { status?: string };
    const list = db.memberPackages.filter(item => !params.status || params.status === 'all' || item.status === params.status);
    return mockSuccess(list);
  },
  // 模拟保存会员套餐
  savePackage(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as MemberPackageRecord;
    const index = db.memberPackages.findIndex(item => item.packageId === data.packageId);
    if (index !== -1) {
      db.memberPackages[index] = { ...db.memberPackages[index], ...data };
      writeMockDb(db);
      appendOperationLog('会员套餐', '编辑', `更新套餐 ${db.memberPackages[index].packageName}`);
      return mockSuccess(db.memberPackages[index]);
    }

    const record = {
      ...data,
      packageId: db.memberPackages.length ? Math.max(...db.memberPackages.map(item => item.packageId)) + 1 : 1,
    };
    db.memberPackages.unshift(record);
    writeMockDb(db);
    appendOperationLog('会员套餐', '新增', `创建套餐 ${record.packageName}`);
    return mockSuccess(record);
  },
  // 模拟删除会员套餐
  deletePackage(packageId: number) {
    const db = readMockDb();
    const index = db.memberPackages.findIndex(item => item.packageId === packageId);
    if (index !== -1) {
      const [removed] = db.memberPackages.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('会员套餐', '删除', `删除套餐 ${removed.packageName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default memberPackagesMock;
