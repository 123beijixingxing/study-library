import { mockSuccess } from '@/mock';
import { appendOperationLog, formatNow, readMockDb, writeMockDb } from '@/mock/db';
import type { ServiceRecord } from '@/types/admin';

const servicesMock = {
  getList(payload: Record<string, unknown>) {
    const db = readMockDb();
    const params = (payload.params || {}) as { keyword?: string; status?: string };
    const list = db.serviceTickets.filter(item => {
      const matchKeyword = !params.keyword || item.userName.includes(params.keyword) || item.latestMessage.includes(params.keyword);
      const matchStatus = !params.status || params.status === 'all' || item.status === params.status;
      return matchKeyword && matchStatus;
    });
    return mockSuccess(list);
  },
  replyTicket(payload: Record<string, unknown>) {
    const db = readMockDb();
    const data = payload.data as { ticketId: number; replyContent?: string; status?: ServiceRecord['status'] };
    const target = db.serviceTickets.find(item => item.id === data.ticketId);
    if (target) {
      if (data.replyContent) {
        target.latestMessage = data.replyContent;
      }
      if (data.status) {
        target.status = data.status;
      } else if (data.replyContent) {
        target.status = 'processing';
      }
      target.updateTime = formatNow();
      writeMockDb(db);
      appendOperationLog('客服管理', '编辑', `处理工单 ${target.id}`);
    }
    return mockSuccess(target || null);
  },
  deleteTicket(id: number) {
    const db = readMockDb();
    const index = db.serviceTickets.findIndex(item => item.id === id);
    if (index !== -1) {
      const [removed] = db.serviceTickets.splice(index, 1);
      writeMockDb(db);
      appendOperationLog('客服管理', '删除', `删除工单 ${removed.id}`);
      return mockSuccess({ success: true });
    }
    return mockSuccess({ success: false });
  },
};

export default servicesMock;
