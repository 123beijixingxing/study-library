<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">园地管理</h1>
          <p class="page-desc">承接火热内容、分享内容、收藏管理与审核动作，方便后台运营统一处理社区内容。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="goHotAudit">火热审核</el-button>
          <el-button @click="goShareAudit">分享审核</el-button>
          <el-button @click="goReportAudit">举报审核</el-button>
          <el-button type="primary" @click="fetchList">刷新列表</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-radio-group v-model="query.contentType" @change="resetPageAndFetch">
          <el-radio-button label="hot">火热内容</el-radio-button>
          <el-radio-button label="share">分享内容</el-radio-button>
        </el-radio-group>
        <el-select v-model="query.auditStatus" @change="resetPageAndFetch">
          <el-option v-for="item in gardenAuditOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="sourceName" label="来源" min-width="140" />
        <el-table-column prop="authorName" label="作者" min-width="120" />
        <el-table-column prop="contentText" label="内容摘要" min-width="300" show-overflow-tooltip />
        <el-table-column prop="likeCount" label="点赞数" width="90" />
        <el-table-column prop="commentCount" label="评论数" width="90" />
        <el-table-column label="审核状态" width="110">
          <template #default="scope">
            <el-tag :type="gardenAuditTagMap[scope.row.auditStatus]">
              {{ gardenAuditTextMap[scope.row.auditStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" min-width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="success" @click="audit(scope.row, 'approved')">通过</el-button>
            <el-button link type="danger" @click="audit(scope.row, 'rejected')">拒绝</el-button>
            <el-button link type="danger" @click="removeContent(scope.row)">删除</el-button>
            <el-button link @click="preview(scope.row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchList" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { gardenHttp } from '@/api/modules/garden';
import { GARDEN_AUDIT_OPTIONS, GARDEN_AUDIT_TAG_MAP, GARDEN_AUDIT_TEXT_MAP } from '@/constants/enums';
import type { GardenRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const loading = ref(false);
const total = ref(0);
const tableData = ref<GardenRecord[]>([]);
const gardenAuditOptions = GARDEN_AUDIT_OPTIONS;
const gardenAuditTextMap = GARDEN_AUDIT_TEXT_MAP;
const gardenAuditTagMap = GARDEN_AUDIT_TAG_MAP;
const pageLayout = DEFAULT_PAGE_LAYOUT;

const query = reactive(createPageQuery({
  contentType: 'hot' as GardenRecord['contentType'],
  auditStatus: 'all',
}));

const fetchList = async () => {
  loading.value = true;
  try {
    const res = await gardenHttp.getContentList({ params: query });
    const pageResult = normalizePageList<GardenRecord>(res.data, query.pageNum, query.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
  } finally {
    loading.value = false;
  }
};

// 重置页码并刷新列表
const resetPageAndFetch = () => {
  query.pageNum = 1;
  fetchList();
};

// 审核园地内容
const audit = async (row: GardenRecord, auditStatus: GardenRecord['auditStatus']) => {
  await gardenHttp.auditContent(row.id, { data: { auditStatus } });
  ElMessage.success(`内容已${gardenAuditTextMap[auditStatus]}`);
  fetchList();
};

// 预览园地内容
const preview = (row: GardenRecord) => {
  ElMessage.info(`预览内容：${row.contentText}`);
};

// 删除园地内容
const removeContent = (row: GardenRecord) =>
  confirmDangerAction({
    message: `确认删除内容 ${row.sourceName} 吗？`,
    successMessage: `已删除内容 ${row.sourceName}`,
    onConfirm: () => gardenHttp.deleteContent(row.id),
    onSuccess: fetchList,
  });

// 跳转举报审核页面
const goReportAudit = () => {
  router.push('/report-audit');
};

// 跳转火热内容审核页面
const goHotAudit = () => {
  router.push('/garden-manage/hot-audit');
};

// 跳转分享内容审核页面
const goShareAudit = () => {
  router.push('/garden-manage/share-audit');
};

onMounted(fetchList);
</script>

<style scoped lang="scss">
.toolbar-head,
.toolbar-actions,
.filter-grid,
.pagination-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.toolbar-actions {
  justify-content: flex-end;
}

.filter-grid {
  margin-top: 20px;
}

.pagination-wrap {
  margin-top: 16px;
  justify-content: flex-end;
}

@media (max-width: 960px) {
  .toolbar-head,
  .filter-grid {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
