<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">举报审核</h1>
          <p class="page-desc">集中处理内容举报、评论举报和投诉反馈，便于后台完成违规内容审核和处理闭环。</p>
        </div>
        <el-button type="primary" @click="fetchReports">刷新举报</el-button>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索举报目标 / 举报人" clearable />
        <el-select v-model="query.reportType">
          <el-option label="全部举报类型" value="all" />
          <el-option label="内容举报" value="内容举报" />
          <el-option label="评论举报" value="评论举报" />
          <el-option label="投诉反馈" value="投诉反馈" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in reportStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchReports">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="reportType" label="举报类型" min-width="120" />
        <el-table-column prop="targetTitle" label="举报目标" min-width="220" />
        <el-table-column prop="reporterName" label="举报人" min-width="120" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="reportStatusTagMap[scope.row.status]">{{ reportStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportTime" label="举报时间" min-width="180" />
        <el-table-column prop="handleResult" label="处理结果" min-width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row, 'handled')">处理</el-button>
            <el-button link @click="openDialog(scope.row, 'ignored')">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchReports" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" title="处理举报" width="560px">
      <el-form :model="formData" label-width="96px">
        <el-form-item label="举报目标">
          <el-input v-model="formData.targetTitle" disabled />
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="已处理" value="handled" />
            <el-option label="已忽略" value="ignored" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="formData.handleResult" />
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input v-model="formData.handleRemark" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveReport">保存处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { reportHttp } from '@/api/modules/reports';
import { REPORT_STATUS_OPTIONS, REPORT_STATUS_TAG_MAP, REPORT_STATUS_TEXT_MAP } from '@/constants/enums';
import type { ReportRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';

const loading = ref(false);
const dialogVisible = ref(false);
const total = ref(0);
const currentReportId = ref<number | null>(null);
const tableData = ref<ReportRecord[]>([]);
const pageLayout = DEFAULT_PAGE_LAYOUT;

const reportStatusOptions = REPORT_STATUS_OPTIONS;
const reportStatusTextMap = REPORT_STATUS_TEXT_MAP;
const reportStatusTagMap = REPORT_STATUS_TAG_MAP;

const query = reactive(createPageQuery({
  keyword: '',
  reportType: 'all',
  status: 'all',
}));

const defaultForm = (): ReportRecord => ({
  reportId: 0,
  reportType: '内容举报',
  targetId: 0,
  targetTitle: '',
  reporterName: '',
  status: 'handled',
  reportTime: '',
  handleResult: '',
  handleRemark: '',
});

const formData = reactive(defaultForm());

const fetchReports = async () => {
  loading.value = true;
  try {
    const res = await reportHttp.getReportList({ params: query });
    const pageResult = normalizePageList<ReportRecord>(res.data, query.pageNum, query.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.keyword = '';
  query.reportType = 'all';
  query.status = 'all';
  query.pageNum = 1;
  fetchReports();
};

const openDialog = (row: ReportRecord, status: ReportRecord['status']) => {
  currentReportId.value = row.reportId;
  Object.assign(formData, row, { status });
  if (status === 'handled' && !formData.handleResult) {
    formData.handleResult = '已处理违规内容';
  }
  if (status === 'ignored' && !formData.handleResult) {
    formData.handleResult = '已忽略举报';
  }
  dialogVisible.value = true;
};

const saveReport = async () => {
  if (!currentReportId.value) {
    return;
  }
  await reportHttp.handleReport(currentReportId.value, { data: formData });
  ElMessage.success('举报处理结果已保存');
  dialogVisible.value = false;
  fetchReports();
};

onMounted(fetchReports);
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
  grid-template-columns: 2fr 1fr 1fr auto;
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
