<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">消息管理</h1>
          <p class="page-desc">统一管理系统消息、活动消息与服务消息，支持草稿、发送和定时发布状态维护。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="goTemplatePage">模板配置</el-button>
          <el-button type="primary" @click="openDialog(null)">新建消息</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索消息标题" clearable />
        <el-select v-model="query.sendStatus">
          <el-option v-for="item in messageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchMessages">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="title" label="消息标题" min-width="220" />
        <el-table-column prop="messageType" label="消息类型" min-width="120" />
        <el-table-column prop="receiverType" label="接收对象" min-width="120" />
        <el-table-column label="发送状态" width="120">
          <template #default="scope">
            <el-tag :type="messageStatusTagMap[scope.row.sendStatus]">
              {{ messageStatusTextMap[scope.row.sendStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sendTime" label="发送时间" min-width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="sendNow(scope.row)">立即发送</el-button>
            <el-button link type="danger" @click="removeMessage(scope.row)">删除</el-button>
            <el-button link type="warning" @click="openTemplateTip">模板配置</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchMessages" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentMessageId ? '编辑消息' : '新建消息'" width="560px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="消息标题">
          <el-input v-model="formData.title" />
        </el-form-item>
        <el-form-item label="消息类型">
          <el-select v-model="formData.messageType" style="width: 100%">
            <el-option label="系统消息" value="系统消息" />
            <el-option label="活动消息" value="活动消息" />
            <el-option label="服务消息" value="服务消息" />
          </el-select>
        </el-form-item>
        <el-form-item label="接收对象">
          <el-select v-model="formData.receiverType" style="width: 100%">
            <el-option label="全部用户" value="全部用户" />
            <el-option label="VIP用户" value="VIP用户" />
            <el-option label="管理员" value="管理员" />
          </el-select>
        </el-form-item>
        <el-form-item label="发送状态">
          <el-select v-model="formData.sendStatus" style="width: 100%">
            <el-option label="草稿" value="draft" />
            <el-option label="已发送" value="sent" />
            <el-option label="定时中" value="scheduled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMessage">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { messageHttp } from '@/api/modules/messages';
import { MESSAGE_STATUS_OPTIONS, MESSAGE_STATUS_TAG_MAP, MESSAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { MessageRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<MessageRecord, { keyword: string; sendStatus: string }>({
  keyword: '',
  sendStatus: 'all',
});

const messageStatusOptions = MESSAGE_STATUS_OPTIONS;
const messageStatusTextMap = MESSAGE_STATUS_TEXT_MAP;
const messageStatusTagMap = MESSAGE_STATUS_TAG_MAP;

const defaultForm = (): MessageRecord => ({
  id: 0,
  title: '',
  messageType: '系统消息',
  receiverType: '全部用户',
  sendStatus: 'draft',
  sendTime: '2026-04-30 10:00:00',
});

const {
  dialogVisible,
  currentKey: currentMessageId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<MessageRecord, number>(() => defaultForm(), row => row.id);

// 获取消息列表
const fetchMessages = async () => run(() => messageHttp.getMessageList({ params: query }));

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchMessages();
};

// 保存消息
const saveMessage = async () => {
  await messageHttp.saveMessage({ data: formData });
  ElMessage.success(currentMessageId.value ? '消息已更新' : '消息已创建');
  closeDialog();
  fetchMessages();
};

// 立即发送消息
const sendNow = async (row: MessageRecord) => {
  await messageHttp.saveMessage({ data: { ...row, sendStatus: 'sent' } });
  ElMessage.success(`消息《${row.title}》已发送`);
  fetchMessages();
};

// 删除消息
const removeMessage = (row: MessageRecord) =>
  confirmDangerAction({
    message: `确认删除消息《${row.title}》吗？`,
    successMessage: `已删除消息《${row.title}》`,
    onConfirm: () => messageHttp.deleteMessage(row.id),
    onSuccess: fetchMessages,
  });

// 打开模板配置页面提示
const openTemplateTip = () => {
  router.push('/message-template-manage');
};

// 跳转消息模板页面
const goTemplatePage = () => {
  router.push('/message-template-manage');
};

onMounted(fetchMessages);
</script>

<style scoped lang="scss">
.toolbar-head,
.toolbar-actions,
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

.toolbar-actions,
.filter-actions {
  gap: 12px;
}

.filter-grid {
  margin-top: 20px;
  display: grid;
  gap: 12px;
  grid-template-columns: 2fr 1fr auto;
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
