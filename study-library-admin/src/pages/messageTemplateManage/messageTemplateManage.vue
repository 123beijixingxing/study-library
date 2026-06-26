<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">消息模板配置</h1>
          <p class="page-desc">统一管理站内信、短信、邮件等消息模板，便于消息中心按渠道发送和预览。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增模板</el-button>
      </div>

      <div class="filter-grid">
        <el-select v-model="query.messageType">
          <el-option label="全部消息类型" value="all" />
          <el-option label="系统消息" value="系统消息" />
          <el-option label="活动消息" value="活动消息" />
          <el-option label="服务消息" value="服务消息" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in packageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchTemplates">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="templateName" label="模板名称" min-width="180" />
        <el-table-column prop="messageType" label="消息类型" min-width="120" />
        <el-table-column prop="titleTemplate" label="标题模板" min-width="220" />
        <el-table-column label="发送渠道" min-width="180">
          <template #default="scope">{{ scope.row.channelList.join('、') }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="packageStatusTagMap[scope.row.status]">{{ packageStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removeTemplate(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchTemplates" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑模板' : '新增模板'" width="620px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="模板名称"><el-input v-model="formData.templateName" /></el-form-item>
        <el-form-item label="消息类型">
          <el-select v-model="formData.messageType" style="width: 100%">
            <el-option label="系统消息" value="系统消息" />
            <el-option label="活动消息" value="活动消息" />
            <el-option label="服务消息" value="服务消息" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题模板"><el-input v-model="formData.titleTemplate" /></el-form-item>
        <el-form-item label="内容模板"><el-input v-model="formData.contentTemplate" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="发送渠道">
          <el-checkbox-group v-model="formData.channelList">
            <el-checkbox label="站内信" />
            <el-checkbox label="短信" />
            <el-checkbox label="邮件" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveTemplate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { messageTemplateHttp } from '@/api/modules/messageTemplates';
import { ENABLE_STATUS_OPTIONS, PACKAGE_STATUS_TAG_MAP, PACKAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { MessageTemplateRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<MessageTemplateRecord, {
  messageType: string;
  status: string;
}>({
  messageType: 'all',
  status: 'all',
});

const packageStatusOptions = ENABLE_STATUS_OPTIONS;
const packageStatusTextMap = PACKAGE_STATUS_TEXT_MAP;
const packageStatusTagMap = PACKAGE_STATUS_TAG_MAP;

const defaultForm = (): MessageTemplateRecord => ({
  templateId: 0,
  templateName: '',
  messageType: '系统消息',
  titleTemplate: '',
  contentTemplate: '',
  channelList: ['站内信'],
  status: 'enabled',
  updateTime: '',
});

const {
  dialogVisible,
  currentKey: currentId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<MessageTemplateRecord, number>(() => defaultForm(), row => row.templateId);

const fetchTemplates = async () => run(() => messageTemplateHttp.getTemplateList({ params: query }));

const resetQuery = () => {
  resetPageQuery();
  fetchTemplates();
};

const saveTemplate = async () => {
  await messageTemplateHttp.saveTemplate({ data: formData });
  ElMessage.success(currentId.value ? '模板已更新' : '模板已创建');
  closeDialog();
  fetchTemplates();
};

const toggleStatus = async (row: MessageTemplateRecord) => {
  const status: MessageTemplateRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await messageTemplateHttp.saveTemplate({ data: { ...row, status } });
  ElMessage.success(`模板状态已更新为${packageStatusTextMap[status]}`);
  fetchTemplates();
};

const removeTemplate = (row: MessageTemplateRecord) =>
  confirmDangerAction({
    message: `确认删除模板 ${row.templateName} 吗？`,
    successMessage: `已删除模板 ${row.templateName}`,
    onConfirm: () => messageTemplateHttp.deleteTemplate(row.templateId),
    onSuccess: fetchTemplates,
  });

onMounted(fetchTemplates);
</script>

<style scoped lang="scss">
.toolbar-head,
.filter-grid,
.filter-actions,
.pagination-wrap {
  display: flex;
  align-items: center;
}

.toolbar-head {
  justify-content: space-between;
  gap: 20px;
}

.filter-grid {
  margin-top: 20px;
  gap: 12px;
  display: grid;
  grid-template-columns: 1fr 1fr auto;
}

.filter-actions {
  gap: 12px;
}

.pagination-wrap {
  margin-top: 16px;
  justify-content: flex-end;
}

@media (max-width: 960px) {
  .toolbar-head {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-grid {
    grid-template-columns: 1fr;
  }
}
</style>
