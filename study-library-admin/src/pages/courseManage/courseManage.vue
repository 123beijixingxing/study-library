<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">课程管理</h1>
          <p class="page-desc">管理课程基础信息、章节规模、状态和后续课程详情配置入口。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="goEvaluationPage">课程评价</el-button>
          <el-button type="primary" @click="openDrawer(null)">新增课程</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索课程名称" clearable />
        <el-select v-model="query.categoryName">
          <el-option label="全部分类" value="all" />
          <el-option label="编程开发" value="编程开发" />
          <el-option label="数据科学" value="数据科学" />
          <el-option label="产品设计" value="产品设计" />
        </el-select>
        <el-select v-model="query.status">
          <el-option v-for="item in courseStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchCourses">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="categoryName" label="所属分类" min-width="120" />
        <el-table-column prop="chapterCount" label="章节数" width="100" />
        <el-table-column prop="hotScore" label="热度" width="100" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="courseStatusTagMap[scope.row.status]">
              {{ courseStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDrawer(scope.row)">编辑</el-button>
            <el-button link @click="changeStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removeCourse(scope.row)">删除</el-button>
            <el-button link type="warning" @click="openChapterTip(scope.row)">章节管理</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-drawer v-model="drawerVisible" :title="currentCourseId ? '编辑课程' : '新增课程'" size="520px">
      <el-form :model="formData" label-width="90px">
        <el-form-item label="课程名称">
          <el-input v-model="formData.courseName" />
        </el-form-item>
        <el-form-item label="所属分类">
          <el-select v-model="formData.categoryName" style="width: 100%">
            <el-option label="编程开发" value="编程开发" />
            <el-option label="数据科学" value="数据科学" />
            <el-option label="产品设计" value="产品设计" />
          </el-select>
        </el-form-item>
        <el-form-item label="章节数">
          <el-input-number v-model="formData.chapterCount" :min="1" :max="99" style="width: 100%" />
        </el-form-item>
        <el-form-item label="热度值">
          <el-input-number v-model="formData.hotScore" :min="0" :max="999" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="已发布" value="published" />
            <el-option label="草稿" value="draft" />
            <el-option label="待审核" value="pending" />
            <el-option label="已下架" value="offline" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCourse">保存课程</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { courseHttp } from '@/api/modules/courses';
import { COURSE_STATUS_OPTIONS, COURSE_STATUS_TAG_MAP, COURSE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { CourseRecord } from '@/types/admin';
import { usePagedTable } from '@/composables/usePagedTable';
import { useEditDialog } from '@/composables/useEditDialog';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const { loading, tableData, query, run, resetQuery: resetPageQuery } = usePagedTable<CourseRecord, {
  keyword: string;
  categoryName: string;
  status: string;
}>({
  keyword: '',
  categoryName: 'all',
  status: 'all',
});

const courseStatusOptions = COURSE_STATUS_OPTIONS;
const courseStatusTextMap = COURSE_STATUS_TEXT_MAP;
const courseStatusTagMap = COURSE_STATUS_TAG_MAP;

const defaultForm = (): CourseRecord => ({
  id: 0,
  courseName: '',
  categoryName: '编程开发',
  hotScore: 80,
  chapterCount: 10,
  status: 'draft',
  updateTime: '2026-04-30 00:00:00',
});

const {
  dialogVisible: drawerVisible,
  currentKey: currentCourseId,
  formData,
  openDialog: openDrawer,
  closeDialog,
} = useEditDialog<CourseRecord, number>(() => defaultForm(), row => row.id);

// 获取课程列表
const fetchCourses = async () => run(() => courseHttp.getCourseList({ params: query }), response => response.data.list || []);

// 重置筛选条件
const resetQuery = () => {
  resetPageQuery();
  fetchCourses();
};

// 保存课程
const saveCourse = async () => {
  if (currentCourseId.value) {
    await courseHttp.updateCourse(currentCourseId.value, { data: formData });
  } else {
    await courseHttp.createCourse({ data: formData });
  }
  ElMessage.success(currentCourseId.value ? '课程已更新' : '课程已创建');
  closeDialog();
  fetchCourses();
};

// 切换课程状态
const changeStatus = async (row: CourseRecord) => {
  const status: CourseRecord['status'] = row.status === 'published' ? 'offline' : 'published';
  await courseHttp.updateCourse(row.id, { data: { ...row, status } });
  ElMessage.success(`课程状态已更新为${courseStatusTextMap[status]}`);
  fetchCourses();
};

// 删除课程
const removeCourse = (row: CourseRecord) =>
  confirmDangerAction({
    message: `确认删除课程 ${row.courseName} 吗？`,
    successMessage: `已删除课程 ${row.courseName}`,
    onConfirm: () => courseHttp.deleteCourse(row.id),
    onSuccess: fetchCourses,
  });

// 打开章节管理页面
const openChapterTip = (row: CourseRecord) => {
  router.push(`/course-manage/${row.id}/detail`);
};

// 打开课程评价页面
const goEvaluationPage = () => {
  router.push('/course-manage/evaluations');
};

onMounted(fetchCourses);
</script>

<style scoped lang="scss">
.toolbar-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.toolbar-actions {
  display: flex;
  gap: 12px;
}

.filter-grid {
  margin-top: 20px;
  gap: 12px;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr auto;
}

.filter-actions,
.drawer-footer {
  display: flex;
  align-items: center;
  gap: 12px;
}

.drawer-footer {
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
