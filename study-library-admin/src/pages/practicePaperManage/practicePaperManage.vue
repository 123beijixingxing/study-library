<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">练习试卷管理</h1>
          <p class="page-desc">用于维护练习试卷、组卷方式、题目数量与发布状态，后续可扩展练习结果分析。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增试卷</el-button>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索试卷名称 / 课程名称" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in practicePaperStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchPapers">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="paperName" label="试卷名称" min-width="200" />
        <el-table-column prop="paperType" label="组卷方式" min-width="140" />
        <el-table-column prop="courseName" label="所属课程" min-width="160" />
        <el-table-column prop="questionCount" label="题目数" width="100" />
        <el-table-column prop="avgScore" label="平均分" width="100" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="practicePaperStatusTagMap[scope.row.status]">
              {{ practicePaperStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="scope">
            <el-button link @click="openDetail(scope.row)">明细分析</el-button>
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removePaper(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchPapers" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentPaperId ? '编辑试卷' : '新增试卷'" width="560px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="试卷名称">
          <el-input v-model="formData.paperName" />
        </el-form-item>
        <el-form-item label="组卷方式">
          <el-select v-model="formData.paperType" style="width: 100%">
            <el-option label="章节练习" value="章节练习" />
            <el-option label="课程练习" value="课程练习" />
            <el-option label="题库组卷" value="题库组卷" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属课程">
          <el-input v-model="formData.courseName" />
        </el-form-item>
        <el-form-item label="题目数">
          <el-input-number v-model="formData.questionCount" :min="1" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="平均分">
          <el-input-number v-model="formData.avgScore" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="草稿" value="draft" />
            <el-option label="已发布" value="published" />
            <el-option label="已下线" value="offline" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePaper">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { practicePaperHttp } from '@/api/modules/practicePapers';
import { PRACTICE_PAPER_STATUS_OPTIONS, PRACTICE_PAPER_STATUS_TAG_MAP, PRACTICE_PAPER_STATUS_TEXT_MAP } from '@/constants/enums';
import type { PracticePaperRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<PracticePaperRecord, { keyword: string; status: string }>({
  keyword: '',
  status: 'all',
});

const practicePaperStatusOptions = PRACTICE_PAPER_STATUS_OPTIONS;
const practicePaperStatusTextMap = PRACTICE_PAPER_STATUS_TEXT_MAP;
const practicePaperStatusTagMap = PRACTICE_PAPER_STATUS_TAG_MAP;

const defaultForm = (): PracticePaperRecord => ({
  paperId: 0,
  paperName: '',
  paperType: '章节练习',
  courseName: '',
  questionCount: 20,
  status: 'draft',
  avgScore: 75,
  updateTime: '',
});

const {
  dialogVisible,
  currentKey: currentPaperId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<PracticePaperRecord, number>(() => defaultForm(), row => row.paperId);

// 获取试卷列表
const fetchPapers = async () => run(() => practicePaperHttp.getPaperList({ params: query }));

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchPapers();
};

// 保存试卷
const savePaper = async () => {
  await practicePaperHttp.savePaper({ data: formData });
  ElMessage.success(currentPaperId.value ? '试卷已更新' : '试卷已创建');
  closeDialog();
  fetchPapers();
};

// 切换试卷状态
const toggleStatus = async (row: PracticePaperRecord) => {
  const status: PracticePaperRecord['status'] = row.status === 'published' ? 'offline' : 'published';
  await practicePaperHttp.savePaper({ data: { ...row, status } });
  ElMessage.success(`试卷状态已更新为${practicePaperStatusTextMap[status]}`);
  fetchPapers();
};

// 删除试卷
const removePaper = (row: PracticePaperRecord) =>
  confirmDangerAction({
    message: `确认删除试卷 ${row.paperName} 吗？`,
    successMessage: `已删除试卷 ${row.paperName}`,
    onConfirm: () => practicePaperHttp.deletePaper(row.paperId),
    onSuccess: fetchPapers,
  });

// 打开试卷详情分析页面
const openDetail = (row: PracticePaperRecord) => {
  router.push(`/practice-paper-manage/${row.paperId}/detail`);
};

onMounted(fetchPapers);
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
