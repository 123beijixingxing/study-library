<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">课程详情管理</h1>
        <p class="page-desc">维护课程讲师、课程简介和章节信息，为后续课程详情页与学习页提供内容数据。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToList">返回课程列表</el-button>
        <el-button type="primary" @click="saveDetail">保存课程详情</el-button>
      </div>
    </section>

    <section class="page-card">
      <el-form :model="detailForm" label-width="88px">
        <el-form-item label="课程名称">
          <el-input :model-value="courseInfo.courseName" disabled />
        </el-form-item>
        <el-form-item label="讲师名称">
          <el-input v-model="detailForm.teacherName" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="detailForm.summary" type="textarea" :rows="5" />
        </el-form-item>
      </el-form>
    </section>

    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h2 class="section-title">章节列表</h2>
          <p class="page-desc">可新增或编辑课程章节，当前为 mock 演示版本。</p>
        </div>
        <el-button type="primary" @click="openChapterDialog(null)">新增章节</el-button>
      </div>

      <el-table :data="chapterList" border class="table-block">
        <el-table-column prop="title" label="章节标题" min-width="240" />
        <el-table-column prop="durationMinutes" label="时长（分钟）" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'published' ? 'success' : 'info'">
              {{ scope.row.status === 'published' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openChapterDialog(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentChapterId ? '编辑章节' : '新增章节'" width="520px">
      <el-form :model="chapterForm" label-width="88px">
        <el-form-item label="章节标题">
          <el-input v-model="chapterForm.title" />
        </el-form-item>
        <el-form-item label="时长">
          <el-input-number v-model="chapterForm.durationMinutes" :min="1" :max="240" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="chapterForm.status" style="width: 100%">
            <el-option label="草稿" value="draft" />
            <el-option label="已发布" value="published" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveChapter">保存章节</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { courseHttp } from '@/api/modules/courses';
import type { CourseChapterRecord, CourseDetailRecord, CourseRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const dialogVisible = ref(false);
const currentChapterId = ref<number | null>(null);

const courseId = Number(route.params.id);

const courseInfo = reactive<CourseRecord>({
  id: courseId,
  courseName: '',
  categoryName: '',
  hotScore: 0,
  chapterCount: 0,
  status: 'draft',
  updateTime: '',
});

const detailForm = reactive<CourseDetailRecord>({
  courseId,
  teacherName: '',
  summary: '',
  chapterList: [],
});

const chapterList = ref<CourseChapterRecord[]>([]);
const chapterForm = reactive<CourseChapterRecord>({
  id: 0,
  title: '',
  durationMinutes: 15,
  status: 'draft',
});

const fetchDetail = async () => {
  loading.value = true;
  try {
    const res = await courseHttp.getCourseDetail(courseId);
    Object.assign(courseInfo, res.data.course);
    Object.assign(detailForm, res.data.detail);
    chapterList.value = res.data.detail.chapterList;
  } finally {
    loading.value = false;
  }
};

const saveDetail = async () => {
  await courseHttp.saveCourseDetail(courseId, {
    data: {
      courseId,
      teacherName: detailForm.teacherName,
      summary: detailForm.summary,
    },
  });
  ElMessage.success('课程详情已保存');
};

const openChapterDialog = (row: CourseChapterRecord | null) => {
  currentChapterId.value = row?.id || null;
  Object.assign(
    chapterForm,
    row || {
      id: 0,
      title: '',
      durationMinutes: 15,
      status: 'draft',
    }
  );
  dialogVisible.value = true;
};

const saveChapter = async () => {
  await courseHttp.saveCourseChapter(courseId, {
    data: {
      ...chapterForm,
      id: currentChapterId.value || chapterForm.id,
    },
  });
  ElMessage.success(currentChapterId.value ? '章节已更新' : '章节已创建');
  dialogVisible.value = false;
  fetchDetail();
};

const backToList = () => {
  router.push('/course-manage');
};

onMounted(fetchDetail);
</script>

<style scoped lang="scss">
.head-block,
.toolbar-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.head-actions {
  display: flex;
  gap: 12px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

.table-block {
  margin-top: 18px;
}

@media (max-width: 960px) {
  .head-block,
  .toolbar-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
