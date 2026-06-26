<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">推荐位配置</h1>
          <p class="page-desc">维护首页、搜索页、详情页等推荐位资源，控制资源类型、目标内容、排序和启用状态。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增推荐位</el-button>
      </div>

      <div class="filter-grid">
        <el-select v-model="query.pageCode">
          <el-option label="全部页面" value="all" />
          <el-option label="首页" value="home" />
          <el-option label="搜索页" value="search" />
          <el-option label="课程详情" value="courseDetail" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in packageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchSlots">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="slotName" label="推荐位名称" min-width="200" />
        <el-table-column prop="pageCode" label="页面编码" min-width="120" />
        <el-table-column prop="resourceType" label="资源类型" min-width="120" />
        <el-table-column prop="targetTitle" label="目标资源" min-width="220" />
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
            <el-button link type="danger" @click="removeSlot(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchSlots" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentId ? '编辑推荐位' : '新增推荐位'" width="560px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="推荐位名称"><el-input v-model="formData.slotName" /></el-form-item>
        <el-form-item label="页面编码">
          <el-select v-model="formData.pageCode" style="width: 100%">
            <el-option label="首页" value="home" />
            <el-option label="搜索页" value="search" />
            <el-option label="课程详情" value="courseDetail" />
          </el-select>
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="formData.resourceType" style="width: 100%">
            <el-option label="课程" value="course" />
            <el-option label="内容" value="content" />
            <el-option label="分类" value="category" />
            <el-option label="Banner" value="banner" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标资源"><el-input v-model="formData.targetTitle" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="formData.sortNo" :min="1" :max="99" style="width: 100%" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSlot">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { recommendSlotHttp } from '@/api/modules/recommendSlots';
import { ENABLE_STATUS_OPTIONS, PACKAGE_STATUS_TAG_MAP, PACKAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { RecommendSlotRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const { loading, total, tableData, pageLayout, query, run, resetQuery: resetPageQuery } = usePagedTable<RecommendSlotRecord, {
  pageCode: string;
  status: string;
}>({
  pageCode: 'all',
  status: 'all',
});

const packageStatusOptions = ENABLE_STATUS_OPTIONS;
const packageStatusTextMap = PACKAGE_STATUS_TEXT_MAP;
const packageStatusTagMap = PACKAGE_STATUS_TAG_MAP;

const defaultForm = (): RecommendSlotRecord => ({
  slotId: 0,
  slotName: '',
  pageCode: 'home',
  resourceType: 'course',
  targetTitle: '',
  sortNo: 1,
  status: 'enabled',
});

const {
  dialogVisible,
  currentKey: currentId,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<RecommendSlotRecord, number>(() => defaultForm(), row => row.slotId);

const fetchSlots = async () => run(() => recommendSlotHttp.getSlotList({ params: query }));

const resetQuery = () => {
  resetPageQuery();
  fetchSlots();
};

const saveSlot = async () => {
  await recommendSlotHttp.saveSlot({ data: formData });
  ElMessage.success(currentId.value ? '推荐位已更新' : '推荐位已创建');
  closeDialog();
  fetchSlots();
};

const toggleStatus = async (row: RecommendSlotRecord) => {
  const status: RecommendSlotRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await recommendSlotHttp.saveSlot({ data: { ...row, status } });
  ElMessage.success(`推荐位状态已更新为${packageStatusTextMap[status]}`);
  fetchSlots();
};

const removeSlot = (row: RecommendSlotRecord) =>
  confirmDangerAction({
    message: `确认删除推荐位 ${row.slotName} 吗？`,
    successMessage: `已删除推荐位 ${row.slotName}`,
    onConfirm: () => recommendSlotHttp.deleteSlot(row.slotId),
    onSuccess: fetchSlots,
  });

onMounted(fetchSlots);
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
  grid-template-columns: 1fr 1fr auto;
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
