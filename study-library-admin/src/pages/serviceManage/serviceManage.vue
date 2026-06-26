<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">客服管理</h1>
          <p class="page-desc">管理用户咨询工单、会话状态、优先级和客服回复动作，为服务运营提供集中处理入口。</p>
        </div>
        <el-button type="primary" @click="fetchTickets">刷新工单</el-button>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索用户或咨询内容" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in ticketStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchTickets">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="userName" label="咨询用户" min-width="140" />
        <el-table-column prop="latestMessage" label="最新咨询内容" min-width="320" show-overflow-tooltip />
        <el-table-column prop="priorityLevel" label="优先级" width="100" />
        <el-table-column label="工单状态" width="120">
          <template #default="scope">
            <el-tag :type="ticketStatusTagMap[scope.row.status]">
              {{ ticketStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDrawer(scope.row)">回复</el-button>
            <el-button link @click="markProcessing(scope.row)">处理中</el-button>
            <el-button link type="success" @click="closeTicket(scope.row)">关闭工单</el-button>
            <el-button link type="danger" @click="removeTicket(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchTickets" />
      </div>
    </section>

    <el-drawer v-model="drawerVisible" title="回复用户咨询" size="520px">
      <div class="reply-block">
        <div class="reply-item">
          <div class="reply-label">咨询用户</div>
          <div class="reply-value">{{ currentRow?.userName || '-' }}</div>
        </div>
        <div class="reply-item">
          <div class="reply-label">当前咨询</div>
          <div class="reply-value">{{ currentRow?.latestMessage || '-' }}</div>
        </div>
        <el-input v-model="replyContent" type="textarea" :rows="6" placeholder="请输入客服回复内容" />
      </div>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="submitReply">发送回复</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { serviceHttp } from '@/api/modules/services';
import { TICKET_STATUS_OPTIONS, TICKET_STATUS_TAG_MAP, TICKET_STATUS_TEXT_MAP } from '@/constants/enums';
import type { ServiceRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { confirmDangerAction } from '@/utils/confirm';

const drawerVisible = ref(false);
const currentRow = ref<ServiceRecord | null>(null);
const replyContent = ref('');
const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<ServiceRecord, { keyword: string; status: string }>({
  keyword: '',
  status: 'all',
});

const ticketStatusOptions = TICKET_STATUS_OPTIONS;
const ticketStatusTextMap = TICKET_STATUS_TEXT_MAP;
const ticketStatusTagMap = TICKET_STATUS_TAG_MAP;

// 获取工单列表
const fetchTickets = async () => run(() => serviceHttp.getTicketList({ params: query }));

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchTickets();
};

// 打开回复抽屉
const openDrawer = (row: ServiceRecord) => {
  currentRow.value = row;
  replyContent.value = '';
  drawerVisible.value = true;
};

// 提交工单回复
const submitReply = async () => {
  if (!currentRow.value) {
    return;
  }
  await serviceHttp.replyTicket({
    data: {
      ticketId: currentRow.value.id,
      replyContent: replyContent.value,
    },
  });
  ElMessage.success('回复已发送');
  drawerVisible.value = false;
  fetchTickets();
};

// 标记工单为处理中
const markProcessing = async (row: ServiceRecord) => {
  await serviceHttp.replyTicket({ data: { ticketId: row.id, status: 'processing' } });
  ElMessage.success(`工单已标记为${ticketStatusTextMap.processing}`);
  fetchTickets();
};

// 关闭工单
const closeTicket = async (row: ServiceRecord) => {
  await serviceHttp.replyTicket({ data: { ticketId: row.id, status: 'closed' } });
  ElMessage.success(`工单已标记为${ticketStatusTextMap.closed}`);
  fetchTickets();
};

// 删除工单
const removeTicket = (row: ServiceRecord) =>
  confirmDangerAction({
    message: `确认删除工单 ${row.id} 吗？`,
    successMessage: `已删除工单 ${row.id}`,
    onConfirm: () => serviceHttp.deleteTicket(row.id),
    onSuccess: fetchTickets,
  });

onMounted(fetchTickets);
</script>

<style scoped lang="scss">
.toolbar-head,
.filter-grid,
.filter-actions,
.drawer-footer,
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
  display: grid;
  gap: 12px;
  grid-template-columns: 2fr 1fr auto;
}

.filter-actions,
.drawer-footer {
  gap: 12px;
}

.drawer-footer {
  justify-content: flex-end;
}

.pagination-wrap {
  margin-top: 16px;
  justify-content: flex-end;
}

.reply-block {
  display: grid;
  gap: 16px;
}

.reply-item {
  display: grid;
  gap: 6px;
}

.reply-label {
  font-size: 13px;
  color: var(--admin-text-muted);
}

.reply-value {
  line-height: 1.7;
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
