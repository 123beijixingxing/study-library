import { mockSuccess } from '@/mock';
import { appendOperationLog, readMockDb, writeMockDb } from '@/mock/db';
import type { GrowthRuleRecord } from '@/types/admin';

const growthRulesMock = {
  // 模拟获取成长规则列表
  getList() {
    const db = readMockDb();
    return mockSuccess(db.growthRules);
  },
  // 模拟保存成长规则
  saveRule(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as GrowthRuleRecord;
    const index = db.growthRules.findIndex(item => item.ruleId === data.ruleId);
    if (index !== -1) {
      db.growthRules[index] = { ...db.growthRules[index], ...data };
      writeMockDb(db);
      appendOperationLog('成长规则', '编辑', `更新规则 ${db.growthRules[index].ruleName}`);
      return mockSuccess(db.growthRules[index]);
    }
    return mockSuccess(null);
  },
};

export default growthRulesMock;
