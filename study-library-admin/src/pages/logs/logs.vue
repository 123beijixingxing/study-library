<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">日志管理</h1>
          <p class="page-desc">查看后台操作日志与登录日志，便于追踪 mock 环境下的新增、编辑、删除、登录与退出行为。</p>
        </div>
        <el-button type="primary" @click="fetchLogs">刷新日志</el-button>
      </div>

      <el-tabs v-model="activeTab" class="tabs-block" @tab-change="handleTabChange">
        <el-tab-pane label="操作日志" name="operation" />
        <el-tab-pane label="登录日志" name="login" />
      </el-tabs>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索操作人 / 模块 / 内容" clearable />
        <el-select v-if="activeTab === 'operation'" v-model="query.operationType">
          <el-option v-for="item in logTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-else v-model="query.status">
          <el-option v-for="item in loginStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchLogs">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card" v-if="activeTab === 'operation'">
      <el-table :data="operationLogs" v-loading="loading" border>
        <el-table-column prop="operateTime" label="操作时间" min-width="180" />
        <el-table-column prop="operatorName" label="操作人" width="120" />
        <el-table-column prop="operationModule" label="操作模块" min-width="120" />
        <el-table-column prop="operationType" label="操作类型" width="110" />
        <el-table-column prop="operationContent" label="操作内容" min-width="320" />
        <el-table-column prop="ip" label="IP" width="120" />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchLogs" />
      </div>
    </section>

    <section class="page-card" v-else>
      <el-table :data="loginLogs" v-loading="loading" border>
        <el-table-column prop="loginTime" label="登录时间" min-width="180" />
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="loginType" label="登录方式" min-width="120" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="loginStatusTagMap[scope.row.status]">
              {{ loginStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP" width="120" />
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchLogs" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { logsHttp } from '@/api/modules/logs';
import {
  LOGIN_LOG_STATUS_OPTIONS,
  LOGIN_LOG_STATUS_TAG_MAP,
  LOGIN_LOG_STATUS_TEXT_MAP,
  LOG_TYPE_OPTIONS,
} from '@/constants/enums';
import type { LoginLogRecord, OperationLogRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';

const activeTab = ref<'operation' | 'login'>('operation');
const loading = ref(false);
const total = ref(0);
const operationLogs = ref<OperationLogRecord[]>([]);
const loginLogs = ref<LoginLogRecord[]>([]);
const pageLayout = DEFAULT_PAGE_LAYOUT;

const query = reactive(createPageQuery({
  keyword: '',
  operationType: 'all',
  status: 'all',
}));

const logTypeOptions = LOG_TYPE_OPTIONS;
const loginStatusOptions = LOGIN_LOG_STATUS_OPTIONS;
const loginStatusTextMap = LOGIN_LOG_STATUS_TEXT_MAP;
const loginStatusTagMap = LOGIN_LOG_STATUS_TAG_MAP;

const fetchLogs = async () => {
  loading.value = true;
  try {
    if (activeTab.value === 'operation') {
      const res = await logsHttp.getOperationLogs({
        params: {
          keyword: query.keyword,
          operationType: query.operationType,
          pageNum: query.pageNum,
          pageSize: query.pageSize,
        },
      });
      const pageResult = normalizePageList<OperationLogRecord>(res.data, query.pageNum, query.pageSize);
      operationLogs.value = pageResult.list;
      total.value = pageResult.total;
    } else {
      const res = await logsHttp.getLoginLogs({
        params: {
          keyword: query.keyword,
          status: query.status,
          pageNum: query.pageNum,
          pageSize: query.pageSize,
        },
      });
      const pageResult = normalizePageList<LoginLogRecord>(res.data, query.pageNum, query.pageSize);
      loginLogs.value = pageResult.list;
      total.value = pageResult.total;
    }
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.keyword = '';
  query.operationType = 'all';
  query.status = 'all';
  query.pageNum = 1;
  fetchLogs();
};

const handleTabChange = () => {
  query.pageNum = 1;
  fetchLogs();
};

onMounted(fetchLogs);
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

.tabs-block {
  margin-top: 18px;
}

.filter-grid {
  margin-top: 18px;
  gap: 12px;
  display: grid;
  grid-template-columns: 2fr 1fr auto;
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
