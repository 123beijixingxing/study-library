<template>
  <div class="page-shell">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">课程评价管理</h1>
        <p class="page-desc">查看课程评价内容并处理显示、隐藏、删除状态，便于维护课程评论区质量。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToCourse">返回课程管理</el-button>
        <el-button type="primary" @click="fetchList">刷新评价</el-button>
      </div>
    </section>

    <section class="page-card">
      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索课程 / 用户 / 评价内容" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in evaluationStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchList">查询</el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" border class="table-block">
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="username" label="评价用户" min-width="120" />
        <el-table-column prop="score" label="评分" width="90" />
        <el-table-column prop="content" label="评价内容" min-width="320" show-overflow-tooltip />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="evaluationStatusTagMap[scope.row.status]">{{ evaluationStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评价时间" min-width="180" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="updateStatus(scope.row, 'visible')">显示</el-button>
            <el-button link type="warning" @click="updateStatus(scope.row, 'hidden')">隐藏</el-button>
            <el-button link type="danger" @click="updateStatus(scope.row, 'deleted')">删除</el-button>
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
import { courseEvaluationHttp } from '@/api/modules/courseEvaluations';
import {
  COURSE_EVALUATION_STATUS_OPTIONS,
  COURSE_EVALUATION_STATUS_TAG_MAP,
  COURSE_EVALUATION_STATUS_TEXT_MAP,
} from '@/constants/enums';
import type { CourseEvaluationRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';

const router = useRouter();
const loading = ref(false);
const total = ref(0);
const tableData = ref<CourseEvaluationRecord[]>([]);
const pageLayout = DEFAULT_PAGE_LAYOUT;
const evaluationStatusOptions = COURSE_EVALUATION_STATUS_OPTIONS;
const evaluationStatusTextMap = COURSE_EVALUATION_STATUS_TEXT_MAP;
const evaluationStatusTagMap = COURSE_EVALUATION_STATUS_TAG_MAP;

const query = reactive(createPageQuery({
  keyword: '',
  status: 'all',
}));

const fetchList = async () => {
  loading.value = true;
  try {
    const res = await courseEvaluationHttp.getEvaluationList({ params: query });
    const pageResult = normalizePageList<CourseEvaluationRecord>(res.data, query.pageNum, query.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.keyword = '';
  query.status = 'all';
  query.pageNum = 1;
  fetchList();
};

const updateStatus = async (row: CourseEvaluationRecord, status: CourseEvaluationRecord['status']) => {
  await courseEvaluationHttp.updateEvaluationStatus(row.evaluationId, { data: { status } });
  ElMessage.success(`评价状态已更新为${evaluationStatusTextMap[status]}`);
  fetchList();
};

const backToCourse = () => {
  router.push('/course-manage');
};

onMounted(fetchList);
</script>

<style scoped lang="scss">
.head-block,
.head-actions,
.filter-actions,
.pagination-wrap {
  display: flex;
  align-items: center;
}

.head-block {
  justify-content: space-between;
  gap: 20px;
}

.head-actions,
.filter-actions {
  gap: 12px;
}

.filter-grid {
  margin-top: 18px;
  display: grid;
  gap: 12px;
  grid-template-columns: 2fr 1fr auto;
}

.table-block {
  margin-top: 18px;
}

.pagination-wrap {
  margin-top: 16px;
  justify-content: flex-end;
}

@media (max-width: 960px) {
  .head-block {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-grid {
    grid-template-columns: 1fr;
  }
}
</style>
