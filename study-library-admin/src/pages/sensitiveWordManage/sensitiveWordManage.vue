<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">敏感词管理</h1>
          <p class="page-desc">用于配置平台敏感词、替换词与拦截状态，支撑内容审核和风控场景。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增敏感词</el-button>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索敏感词" clearable />
        <el-select v-model="query.status">
          <el-option v-for="item in packageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchWords">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="word" label="敏感词" min-width="180" />
        <el-table-column prop="replaceText" label="替换词" min-width="180" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="packageStatusTagMap[scope.row.status]">{{ packageStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removeWord(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchWords" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑敏感词' : '新增敏感词'" width="520px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="敏感词"><el-input v-model="formData.word" /></el-form-item>
        <el-form-item label="替换词"><el-input v-model="formData.replaceText" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveWord">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { sensitiveWordHttp } from '@/api/modules/sensitiveWords';
import { ENABLE_STATUS_OPTIONS, PACKAGE_STATUS_TAG_MAP, PACKAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { SensitiveWordRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<SensitiveWordRecord, {
  keyword: string;
  status: string;
}>({
  keyword: '',
  status: 'all',
});

const packageStatusOptions = ENABLE_STATUS_OPTIONS;
const packageStatusTextMap = PACKAGE_STATUS_TEXT_MAP;
const packageStatusTagMap = PACKAGE_STATUS_TAG_MAP;

const defaultForm = (): SensitiveWordRecord => ({
  wordId: 0,
  word: '',
  replaceText: '***',
  status: 'enabled',
  updateTime: '',
});

const {
  dialogVisible,
  currentKey: currentId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<SensitiveWordRecord, number>(() => defaultForm(), row => row.wordId);

const fetchWords = async () => run(() => sensitiveWordHttp.getWordList({ params: query }));

const resetQuery = () => {
  resetPageQuery();
  fetchWords();
};

const saveWord = async () => {
  await sensitiveWordHttp.saveWord({ data: formData });
  ElMessage.success(currentId.value ? '敏感词已更新' : '敏感词已创建');
  closeDialog();
  fetchWords();
};

const toggleStatus = async (row: SensitiveWordRecord) => {
  const status: SensitiveWordRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await sensitiveWordHttp.saveWord({ data: { ...row, status } });
  ElMessage.success(`敏感词状态已更新为${packageStatusTextMap[status]}`);
  fetchWords();
};

const removeWord = (row: SensitiveWordRecord) =>
  confirmDangerAction({
    message: `确认删除敏感词 ${row.word} 吗？`,
    successMessage: `已删除敏感词 ${row.word}`,
    onConfirm: () => sensitiveWordHttp.deleteWord(row.wordId),
    onSuccess: fetchWords,
  });

onMounted(fetchWords);
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
