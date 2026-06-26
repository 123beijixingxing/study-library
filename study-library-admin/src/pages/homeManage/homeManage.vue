<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">首页管理</h1>
          <p class="page-desc">统一维护首页推荐课程、热门课程、最新课程和课程分类的展示内容与排序。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="goSearchOperation">搜索运营</el-button>
          <el-button @click="goRecommendSlot">推荐位配置</el-button>
          <el-button type="primary" @click="fetchList">刷新内容</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" class="tabs-block" @tab-change="handleTabChange">
        <el-tab-pane label="推荐课程" name="recommend" />
        <el-tab-pane label="热门课程" name="hot" />
        <el-tab-pane label="最新课程" name="latest" />
        <el-tab-pane label="课程分类" name="category" />
      </el-tabs>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="title" label="展示标题" min-width="180" />
        <el-table-column prop="summary" label="展示简介" min-width="280" />
        <el-table-column prop="hotScore" label="热度" width="100" />
        <el-table-column prop="sortNo" label="排序" width="90" />
        <el-table-column label="显示状态" width="120">
          <template #default="scope">
            <el-tag :type="displayStatusTagMap[scope.row.displayStatus]">
              {{ displayStatusTextMap[scope.row.displayStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleDisplay(scope.row)">{{ scope.row.displayStatus === 'visible' ? '隐藏' : '显示' }}</el-button>
            <el-button link type="danger" @click="removeSection(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pageQuery.pageNum"
          v-model:page-size="pageQuery.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchList" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" title="编辑首页配置" width="520px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="展示标题">
          <el-input v-model="formData.title" />
        </el-form-item>
        <el-form-item label="展示简介">
          <el-input v-model="formData.summary" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="热度值">
          <el-input-number v-model="formData.hotScore" :min="0" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortNo" :min="1" :max="99" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveConfig">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { homeManageHttp } from '@/api/modules/home';
import { DISPLAY_STATUS_TAG_MAP, DISPLAY_STATUS_TEXT_MAP } from '@/constants/enums';
import type { HomeSectionRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const activeTab = ref<HomeSectionRecord['sectionType']>('recommend');
const loading = ref(false);
const total = ref(0);
const tableData = ref<HomeSectionRecord[]>([]);
const displayStatusTextMap = DISPLAY_STATUS_TEXT_MAP;
const displayStatusTagMap = DISPLAY_STATUS_TAG_MAP;
const pageLayout = DEFAULT_PAGE_LAYOUT;
const pageQuery = reactive(createPageQuery({}));

const defaultForm = (): HomeSectionRecord => ({
  id: 0,
  sectionType: 'recommend',
  title: '',
  summary: '',
  hotScore: 0,
  sortNo: 1,
  displayStatus: 'visible',
});

const {
  dialogVisible,
  formData,
  openDialog,
  closeDialog,
} = useEditDialog<HomeSectionRecord, number>(() => defaultForm(), row => row.id);

// 获取首页配置列表
const fetchList = async () => {
  loading.value = true;
  try {
    const res = await homeManageHttp.getSectionList({
      params: { sectionType: activeTab.value, pageNum: pageQuery.pageNum, pageSize: pageQuery.pageSize },
    });
    const pageResult = normalizePageList<HomeSectionRecord>(res.data, pageQuery.pageNum, pageQuery.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
  } finally {
    loading.value = false;
  }
};

// 切换首页配置分组时刷新列表
const handleTabChange = () => {
  pageQuery.pageNum = 1;
  fetchList();
};

// 切换首页内容显示状态
const toggleDisplay = async (row: HomeSectionRecord) => {
  const displayStatus = row.displayStatus === 'visible' ? 'hidden' : 'visible';
  await homeManageHttp.updateSection({ data: { id: row.id, displayStatus } });
  ElMessage.success(displayStatus === 'visible' ? '内容已恢复显示' : '内容已隐藏');
  fetchList();
};

// 保存首页配置
const saveConfig = async () => {
  await homeManageHttp.updateSection({ data: formData });
  ElMessage.success('首页配置已更新');
  closeDialog();
  fetchList();
};

// 删除首页配置
const removeSection = (row: HomeSectionRecord) =>
  confirmDangerAction({
    message: `确认删除首页配置 ${row.title} 吗？`,
    successMessage: `已删除首页配置 ${row.title}`,
    onConfirm: () => homeManageHttp.deleteSection(row.id),
    onSuccess: fetchList,
  });

// 跳转搜索运营页面
const goSearchOperation = () => {
  router.push('/search-operation-manage');
};

// 跳转推荐位页面
const goRecommendSlot = () => {
  router.push('/recommend-slot-manage');
};

onMounted(fetchList);
</script>

<style scoped lang="scss">
.toolbar-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.toolbar-actions,
.pagination-wrap {
  display: flex;
  gap: 12px;
}

.tabs-block {
  margin-top: 18px;
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
}
</style>
