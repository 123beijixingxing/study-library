<template>
  <div class="page-shell">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">学习记录管理</h1>
        <p class="page-desc">查看用户学习记录、学习进度和最近学习时间，并支持后台删除异常记录。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToMember">返回会员管理</el-button>
        <el-button type="primary" @click="fetchList">刷新记录</el-button>
      </div>
    </section>

    <section class="page-card">
      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索用户 / 课程" clearable />
        <el-select v-model="query.status">
          <el-option label="全部状态" value="all" />
          <el-option label="学习中" value="learning" />
          <el-option label="已完成" value="completed" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchList">查询</el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" border class="table-block">
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="progressPercent" label="学习进度" width="120">
          <template #default="scope">{{ scope.row.progressPercent }}%</template>
        </el-table-column>
        <el-table-column prop="lastStudyTime" label="最近学习时间" min-width="180" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="studyRecordStatusTagMap[scope.row.status]">{{ studyRecordStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button link type="danger" @click="removeRecord(scope.row.recordId)">删除记录</el-button>
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
import { useRouter } from 'vue-router';
import { studyRecordHttp } from '@/api/modules/studyRecords';
import { STUDY_RECORD_STATUS_TAG_MAP, STUDY_RECORD_STATUS_TEXT_MAP } from '@/constants/enums';
import type { StudyRecordManageRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const loading = ref(false);
const total = ref(0);
const tableData = ref<StudyRecordManageRecord[]>([]);
const studyRecordStatusTextMap = STUDY_RECORD_STATUS_TEXT_MAP;
const studyRecordStatusTagMap = STUDY_RECORD_STATUS_TAG_MAP;
const pageLayout = DEFAULT_PAGE_LAYOUT;

const query = reactive(createPageQuery({
  keyword: '',
  status: 'all',
}));

const fetchList = async () => {
  loading.value = true;
  try {
    const res = await studyRecordHttp.getStudyRecordList({ params: query });
    const pageResult = normalizePageList<StudyRecordManageRecord>(res.data, query.pageNum, query.pageSize);
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

const removeRecord = (recordId: number) =>
  confirmDangerAction({
    message: '确认删除这条学习记录吗？',
    successMessage: '学习记录已删除',
    onConfirm: () => studyRecordHttp.deleteStudyRecord(recordId),
    onSuccess: fetchList,
  });

const backToMember = () => {
  router.push('/member-manage');
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
