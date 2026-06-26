<template>
  <div class="page-shell">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">火热内容审核</h1>
        <p class="page-desc">集中审核园地中的火热内容，支持按审核状态筛选并快速处理内容展示状态。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToGarden">返回园地管理</el-button>
        <el-button type="primary" @click="fetchList">刷新列表</el-button>
      </div>
    </section>

    <section class="page-card">
      <div class="filter-grid">
        <el-select v-model="query.auditStatus" @change="fetchList">
          <el-option v-for="item in gardenAuditOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>

      <el-table :data="tableData" v-loading="loading" border class="table-block">
        <el-table-column prop="sourceName" label="来源" min-width="140" />
        <el-table-column prop="authorName" label="作者" min-width="120" />
        <el-table-column prop="contentText" label="内容摘要" min-width="320" show-overflow-tooltip />
        <el-table-column prop="likeCount" label="点赞数" width="90" />
        <el-table-column prop="commentCount" label="评论数" width="90" />
        <el-table-column label="审核状态" width="110">
          <template #default="scope">
            <el-tag :type="gardenAuditTagMap[scope.row.auditStatus]">{{ gardenAuditTextMap[scope.row.auditStatus] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" min-width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="success" @click="audit(scope.row, 'approved')">通过</el-button>
            <el-button link type="danger" @click="audit(scope.row, 'rejected')">拒绝</el-button>
            <el-button link @click="preview(scope.row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>
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

const router = useRouter();
const loading = ref(false);
const tableData = ref<GardenRecord[]>([]);
const gardenAuditOptions = GARDEN_AUDIT_OPTIONS;
const gardenAuditTextMap = GARDEN_AUDIT_TEXT_MAP;
const gardenAuditTagMap = GARDEN_AUDIT_TAG_MAP;

const query = reactive({
  contentType: 'hot' as GardenRecord['contentType'],
  auditStatus: 'all',
});

const fetchList = async () => {
  loading.value = true;
  try {
    const res = await gardenHttp.getContentList({ params: query });
    tableData.value = res.data;
  } finally {
    loading.value = false;
  }
};

const audit = async (row: GardenRecord, auditStatus: GardenRecord['auditStatus']) => {
  await gardenHttp.auditContent(row.id, { data: { auditStatus } });
  ElMessage.success(`内容已${gardenAuditTextMap[auditStatus]}`);
  fetchList();
};

const preview = (row: GardenRecord) => {
  ElMessage.info(`预览内容：${row.contentText}`);
};

const backToGarden = () => {
  router.push('/garden-manage');
};

onMounted(fetchList);
</script>

<style scoped lang="scss">
.head-block,
.head-actions {
  display: flex;
  align-items: center;
}

.head-block {
  justify-content: space-between;
  gap: 20px;
}

.head-actions {
  gap: 12px;
}

.table-block,
.filter-grid {
  margin-top: 18px;
}

@media (max-width: 960px) {
  .head-block {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
