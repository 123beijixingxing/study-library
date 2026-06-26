<template>
  <div class="page-shell">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">题目管理</h1>
        <p class="page-desc">维护当前题库下的题目列表、题型、难度和启停状态，并支持批量导入演示数据。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToBanks">返回题库管理</el-button>
        <el-button @click="openImportDialog">批量导入</el-button>
        <el-button type="primary" @click="openEditDialog(null)">新增题目</el-button>
      </div>
    </section>

    <section class="page-card">
      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索题干 / 版本说明" clearable />
        <el-select v-model="query.questionType">
          <el-option v-for="item in questionTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-input v-model="query.versionGroupId" placeholder="搜索版本组 ID" clearable />
        <el-select v-model="query.templateCode">
          <el-option label="全部模板" value="all" />
          <el-option label="单选概念题" value="singleConcept" />
          <el-option label="多选场景题" value="multiScenario" />
          <el-option label="判断基础题" value="judgeBasic" />
          <el-option label="简答分析题" value="shortAnswer" />
          <el-option label="批量导入" value="batchImport" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in questionStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchQuestions">查询</el-button>
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" border class="table-block">
        <el-table-column prop="questionId" label="题目ID" width="90" />
        <el-table-column prop="versionGroupId" label="版本组" min-width="150" />
        <el-table-column prop="chapterName" label="所属章节" min-width="140" />
        <el-table-column prop="versionNo" label="版本" width="80" />
        <el-table-column prop="templateCode" label="模板标识" min-width="140" />
        <el-table-column prop="title" label="题干" min-width="320" show-overflow-tooltip />
        <el-table-column prop="questionType" label="题型" min-width="120" />
        <el-table-column prop="difficulty" label="难度" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="questionStatusTagMap[scope.row.status]">{{ questionStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="340" fixed="right">
          <template #default="scope">
            <el-button link @click="openDetailPage(scope.row)">详情</el-button>
            <el-button link type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button link @click="copyQuestion(scope.row)">复制</el-button>
            <el-button link @click="toggleStatus(scope.row)">{{ scope.row.status === 'enabled' ? '停用' : '启用' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="editDialogVisible" :title="currentQuestionId ? '编辑题目' : '新增题目'" width="620px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="题干">
          <el-input v-model="formData.title" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="题型">
          <el-select v-model="formData.questionType" style="width: 100%">
            <el-option label="单选题" value="单选题" />
            <el-option label="多选题" value="多选题" />
            <el-option label="判断题" value="判断题" />
            <el-option label="简答题" value="简答题" />
          </el-select>
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
            <el-option label="启用" value="enabled" />
            <el-option label="停用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveQuestion">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="importDialogVisible" title="批量导入题目" width="520px">
      <el-form :model="importForm" label-width="96px">
        <el-form-item label="题型">
          <el-select v-model="importForm.questionType" style="width: 100%">
            <el-option label="单选题" value="单选题" />
            <el-option label="多选题" value="多选题" />
            <el-option label="判断题" value="判断题" />
            <el-option label="简答题" value="简答题" />
          </el-select>
        </el-form-item>
        <el-form-item label="导入数量">
          <el-input-number v-model="importForm.count" :min="1" :max="50" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="importQuestions">开始导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { questionHttp } from '@/api/modules/questions';
import { QUESTION_STATUS_OPTIONS, QUESTION_STATUS_TAG_MAP, QUESTION_STATUS_TEXT_MAP, QUESTION_TYPE_OPTIONS } from '@/constants/enums';
import Storage from '@/utils/saveStorage';
import type { QuestionRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const editDialogVisible = ref(false);
const importDialogVisible = ref(false);
const currentQuestionId = ref<number | null>(null);
const tableData = ref<QuestionRecord[]>([]);
const bankId = Number(route.params.id);

const questionTypeOptions = QUESTION_TYPE_OPTIONS;
const questionStatusOptions = QUESTION_STATUS_OPTIONS;
const questionStatusTextMap = QUESTION_STATUS_TEXT_MAP;
const questionStatusTagMap = QUESTION_STATUS_TAG_MAP;

const query = reactive({
  bankId,
  keyword: Storage.readSession<string>(`question_manage_keyword_${bankId}`) || '',
  questionType: Storage.readSession<string>(`question_manage_type_${bankId}`) || 'all',
  versionGroupId: Storage.readSession<string>(`question_manage_group_${bankId}`) || '',
  templateCode: Storage.readSession<string>(`question_manage_template_${bankId}`) || 'all',
  status: Storage.readSession<string>(`question_manage_status_${bankId}`) || 'all',
});

const defaultForm = (): QuestionRecord => ({
  questionId: 0,
  bankId,
  chapterName: '第1章 基础概念',
  templateCode: 'singleConcept',
  versionGroupId: 'QG-NEW',
  versionNo: 1,
  versionRemark: '初始版本',
  sourceAction: 'manual',
  sourceQuestionId: null,
  sourceVersionNo: null,
  title: '',
  questionType: '单选题',
  difficulty: '初级',
  status: 'enabled',
  updateTime: '',
  optionList: [
    { key: 'A', label: '', isCorrect: true },
    { key: 'B', label: '', isCorrect: false },
    { key: 'C', label: '', isCorrect: false },
    { key: 'D', label: '', isCorrect: false },
  ],
  answerText: '',
  analysisText: '',
});

const formData = reactive(defaultForm());
const importForm = reactive({
  bankId,
  questionType: '单选题' as QuestionRecord['questionType'],
  count: 5,
});

const fetchQuestions = async () => {
  loading.value = true;
  try {
    const res = await questionHttp.getQuestionList({ params: query });
    tableData.value = res.data;
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.keyword = '';
  query.questionType = 'all';
  query.versionGroupId = '';
  query.templateCode = 'all';
  query.status = 'all';
  fetchQuestions();
};

const openEditDialog = (row: QuestionRecord | null) => {
  currentQuestionId.value = row?.questionId || null;
  Object.assign(formData, row || defaultForm());
  editDialogVisible.value = true;
};

const saveQuestion = async () => {
  await questionHttp.saveQuestion({ data: formData });
  ElMessage.success(currentQuestionId.value ? '题目已更新' : '题目已创建');
  editDialogVisible.value = false;
  fetchQuestions();
};

const toggleStatus = async (row: QuestionRecord) => {
  const status: QuestionRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await questionHttp.updateQuestionStatus(row.questionId, { data: { status } });
  ElMessage.success(`题目状态已更新为${questionStatusTextMap[status]}`);
  fetchQuestions();
};

const openImportDialog = () => {
  importDialogVisible.value = true;
};

const importQuestions = async () => {
  const res = await questionHttp.importQuestions({ data: importForm });
  ElMessage.success(`成功导入 ${res.data.successCount} 道题目`);
  importDialogVisible.value = false;
  fetchQuestions();
};

const openDetailPage = (row: QuestionRecord) => {
  router.push(`/question-bank-manage/${bankId}/questions/${row.questionId}/detail`);
};

const copyQuestion = async (row: QuestionRecord) => {
  const res = await questionHttp.copyQuestion(row.questionId);
  ElMessage.success(`已复制题目，生成版本 ${res.data.versionNo}`);
  fetchQuestions();
};

const backToBanks = () => {
  router.push('/question-bank-manage');
};

watch(
  () => query.keyword,
  value => Storage.saveSession(`question_manage_keyword_${bankId}`, value)
);

watch(
  () => query.questionType,
  value => Storage.saveSession(`question_manage_type_${bankId}`, value)
);

watch(
  () => query.versionGroupId,
  value => Storage.saveSession(`question_manage_group_${bankId}`, value)
);

watch(
  () => query.templateCode,
  value => Storage.saveSession(`question_manage_template_${bankId}`, value)
);

watch(
  () => query.status,
  value => Storage.saveSession(`question_manage_status_${bankId}`, value)
);

onMounted(fetchQuestions);
</script>

<style scoped lang="scss">
.head-block,
.head-actions,
.filter-actions {
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
  grid-template-columns: 2fr 1fr 1.3fr 1fr 1fr auto;
}

.table-block {
  margin-top: 18px;
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
