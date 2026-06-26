<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">题库管理</h1>
          <p class="page-desc">用于维护题库、分类、题目数量和上线状态，为练习中心和试卷管理提供基础数据。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="goPracticePapers">练习试卷</el-button>
          <el-button type="primary" @click="openDialog(null)">新增题库</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索题库名称 / 分类" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in questionBankStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchQuestionBanks">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="bankName" label="题库名称" min-width="200" />
        <el-table-column prop="categoryName" label="所属分类" min-width="140" />
        <el-table-column prop="questionCount" label="题目数量" width="120" />
        <el-table-column prop="difficulty" label="难度" width="100" />
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag :type="questionBankStatusTagMap[scope.row.status]">
              {{ questionBankStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button link @click="goQuestions(scope.row)">题目列表</el-button>
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removeQuestionBank(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchQuestionBanks" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑题库' : '新增题库'" width="520px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="题库名称">
          <el-input v-model="formData.bankName" />
        </el-form-item>
        <el-form-item label="所属分类">
          <el-input v-model="formData.categoryName" />
        </el-form-item>
        <el-form-item label="题目数量">
          <el-input-number v-model="formData.questionCount" :min="1" :max="9999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="难度">
          <el-select v-model="formData.difficulty" style="width: 100%">
            <el-option label="初级" value="初级" />
            <el-option label="中级" value="中级" />
            <el-option label="高级" value="高级" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="草稿" value="draft" />
            <el-option label="已上线" value="online" />
            <el-option label="已下线" value="offline" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuestionBank">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { questionBankHttp } from '@/api/modules/questionBanks';
import {
  QUESTION_BANK_STATUS_OPTIONS,
  QUESTION_BANK_STATUS_TAG_MAP,
  QUESTION_BANK_STATUS_TEXT_MAP,
} from '@/constants/enums';
import type { QuestionBankRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<QuestionBankRecord, { keyword: string; status: string }>({
  keyword: '',
  status: 'all',
});

const questionBankStatusOptions = QUESTION_BANK_STATUS_OPTIONS;
const questionBankStatusTextMap = QUESTION_BANK_STATUS_TEXT_MAP;
const questionBankStatusTagMap = QUESTION_BANK_STATUS_TAG_MAP;

const defaultForm = (): QuestionBankRecord => ({
  id: 0,
  bankName: '',
  categoryName: 'Python',
  questionCount: 50,
  difficulty: '初级',
  status: 'draft',
  updateTime: '',
});

const {
  dialogVisible,
  currentKey: currentId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<QuestionBankRecord, number>(() => defaultForm(), row => row.id);

// 获取题库列表
const fetchQuestionBanks = async () => run(() => questionBankHttp.getQuestionBankList({ params: query }));

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchQuestionBanks();
};

// 保存题库
const saveQuestionBank = async () => {
  await questionBankHttp.saveQuestionBank({ data: formData });
  ElMessage.success(currentId.value ? '题库已更新' : '题库已创建');
  closeDialog();
  fetchQuestionBanks();
};

// 切换题库状态
const toggleStatus = async (row: QuestionBankRecord) => {
  const status: QuestionBankRecord['status'] = row.status === 'online' ? 'offline' : 'online';
  await questionBankHttp.saveQuestionBank({ data: { ...row, status } });
  ElMessage.success(`题库状态已更新为${questionBankStatusTextMap[status]}`);
  fetchQuestionBanks();
};

// 删除题库
const removeQuestionBank = (row: QuestionBankRecord) =>
  confirmDangerAction({
    message: `确认删除题库 ${row.bankName} 吗？`,
    successMessage: `已删除题库 ${row.bankName}`,
    onConfirm: () => questionBankHttp.deleteQuestionBank(row.id),
    onSuccess: fetchQuestionBanks,
  });

// 跳转练习试卷页面
const goPracticePapers = () => {
  router.push('/practice-paper-manage');
};

// 跳转题目管理页面
const goQuestions = (row: QuestionBankRecord) => {
  router.push(`/question-bank-manage/${row.id}/questions`);
};

onMounted(fetchQuestionBanks);
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
  gap: 12px;
  display: grid;
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
