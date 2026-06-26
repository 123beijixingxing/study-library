<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">搜索运营配置</h1>
          <p class="page-desc">维护热搜词、联想词、同义词和生效场景，用于首页、搜索页和详情页的搜索运营配置。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增关键词</el-button>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索关键词" clearable />
        <el-select v-model="query.scene">
          <el-option label="全部场景" value="all" />
          <el-option label="首页" value="home" />
          <el-option label="搜索页" value="search" />
          <el-option label="详情页" value="detail" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in packageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchKeywords">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="keyword" label="关键词" min-width="180" />
        <el-table-column prop="scene" label="生效场景" min-width="120" />
        <el-table-column prop="synonymText" label="同义词" min-width="220" />
        <el-table-column prop="effectTimeRange" label="生效周期" min-width="140" />
        <el-table-column prop="sortNo" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="packageStatusTagMap[scope.row.status]">{{ packageStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removeKeyword(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchKeywords" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑关键词' : '新增关键词'" width="560px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="关键词">
          <el-input v-model="formData.keyword" />
        </el-form-item>
        <el-form-item label="生效场景">
          <el-select v-model="formData.scene" style="width: 100%">
            <el-option label="首页" value="home" />
            <el-option label="搜索页" value="search" />
            <el-option label="详情页" value="detail" />
          </el-select>
        </el-form-item>
        <el-form-item label="同义词">
          <el-input v-model="formData.synonymText" />
        </el-form-item>
        <el-form-item label="生效周期">
          <el-input v-model="formData.effectTimeRange" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortNo" :min="1" :max="99" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveKeyword">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { searchOperationHttp } from '@/api/modules/searchOperations';
import { ENABLE_STATUS_OPTIONS, PACKAGE_STATUS_TAG_MAP, PACKAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { SearchOperationRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<SearchOperationRecord, {
  keyword: string;
  scene: string;
  status: string;
}>({
  keyword: '',
  scene: 'all',
  status: 'all',
});

const packageStatusOptions = ENABLE_STATUS_OPTIONS;
const packageStatusTextMap = PACKAGE_STATUS_TEXT_MAP;
const packageStatusTagMap = PACKAGE_STATUS_TAG_MAP;

const defaultForm = (): SearchOperationRecord => ({
  keywordId: 0,
  keyword: '',
  scene: 'home',
  sortNo: 1,
  status: 'enabled',
  effectTimeRange: '长期生效',
  synonymText: '',
});

const {
  dialogVisible,
  currentKey: currentId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<SearchOperationRecord, number>(() => defaultForm(), row => row.keywordId);

const fetchKeywords = async () => run(() => searchOperationHttp.getKeywordList({ params: query }));

const resetQuery = () => {
  resetPageQuery();
  fetchKeywords();
};

const saveKeyword = async () => {
  await searchOperationHttp.saveKeyword({ data: formData });
  ElMessage.success(currentId.value ? '关键词已更新' : '关键词已创建');
  closeDialog();
  fetchKeywords();
};

const toggleStatus = async (row: SearchOperationRecord) => {
  const status: SearchOperationRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await searchOperationHttp.saveKeyword({ data: { ...row, status } });
  ElMessage.success(`关键词状态已更新为${packageStatusTextMap[status]}`);
  fetchKeywords();
};

const removeKeyword = (row: SearchOperationRecord) =>
  confirmDangerAction({
    message: `确认删除热搜词 ${row.keyword} 吗？`,
    successMessage: `已删除热搜词 ${row.keyword}`,
    onConfirm: () => searchOperationHttp.deleteKeyword(row.keywordId),
    onSuccess: fetchKeywords,
  });

onMounted(fetchKeywords);
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
