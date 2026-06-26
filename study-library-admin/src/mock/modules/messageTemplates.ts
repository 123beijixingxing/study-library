import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, readMockDb, writeMockDb } from '@/mock/db';
import type { MessageTemplateRecord } from '@/types/admin';

const messageTemplatesMock = {
  // 模拟获取消息模板列表
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { messageType?: string; status?: string };
    const list = db.messageTemplates.filter(item => {
      const matchType = !params.messageType || params.messageType === 'all' || item.messageType === params.messageType;
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchType && matchStatus;
    });
    return mockSuccess(list);
  },
  // 模拟保存消息模板
  saveTemplate(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as MessageTemplateRecord;
    const index = db.messageTemplates.findIndex(item => item.templateId === data.templateId);
    if (index !== -1) {
      db.messageTemplates[index] = { ...db.messageTemplates[index], ...data };
      writeMockDb(db);
      appendOperationLog('消息模板', '编辑', `更新模板 ${db.messageTemplates[index].templateName}`);
      return mockSuccess(db.messageTemplates[index]);
    }

    const record = {
      ...data,
      templateId: db.messageTemplates.length ? Math.max(...db.messageTemplates.map(item => item.templateId)) + 1 : 1,
      updateTime: formatNow(),
    };
    db.messageTemplates.unshift(record);
    writeMockDb(db);
    appendOperationLog('消息模板', '新增', `创建模板 ${record.templateName}`);
    return mockSuccess(record);
  },
  // 模拟删除消息模板
  deleteTemplate(templateId: number) {
    const db = readMockDb();
    const index = db.messageTemplates.findIndex(item => item.templateId === templateId);
    if (index !== -1) {
      const [removed] = db.messageTemplates.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('消息模板', '删除', `删除模板 ${removed.templateName}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default messageTemplatesMock;
