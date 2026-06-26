import { adaptMessageTemplateList, serializeMessageTemplatePayload } from '@/api/adapters/admin';
import apiConfig from '@/api/utils/apiConfig';
import { applyAdapter } from '@/api/utils/adapter';
import { isMockEnabled } from '@/mock';
import messageTemplatesMock from '@/mock/modules/messageTemplates';

const { axios, apiHeader, rest } = apiConfig;

export const messageTemplateHttp = {
  // 获取消息模板列表
  getTemplateList(object: Record<string, unknown> = {}) {
    const request = isMockEnabled() ? messageTemplatesMock.getList(object) : axios({ method: 'get', url: `${apiHeader}/message-templates`, ...rest(object) });
    return applyAdapter(request, adaptMessageTemplateList);
  },
  // 保存消息模板
  saveTemplate(object: Record<string, unknown>) {
    const payload = { ...object, data: serializeMessageTemplatePayload(object.data) };
    return isMockEnabled() ? messageTemplatesMock.saveTemplate(payload) : axios({ method: 'post', url: `${apiHeader}/message-templates`, ...rest(payload) });
  },
  // 删除消息模板
  deleteTemplate(templateId: number) {
    return isMockEnabled() ? messageTemplatesMock.deleteTemplate(templateId) : axios({ method: 'delete', url: `${apiHeader}/message-templates/${templateId}` });
  },
};

export default messageTemplateHttp;
