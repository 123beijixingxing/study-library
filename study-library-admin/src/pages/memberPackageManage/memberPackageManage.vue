<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">会员套餐配置</h1>
          <p class="page-desc">配置会员套餐名称、价格、时长、权益和启用状态，作为会员运营体系的基础配置。</p>
        </div>
        <el-button type="primary" @click="openDialog(null)">新增套餐</el-button>
      </div>

      <div class="filter-grid">
        <el-select v-model="query.status">
          <el-option v-for="item in packageStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchPackages">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="packageName" label="套餐名称" min-width="180" />
        <el-table-column prop="price" label="价格" width="100" />
        <el-table-column prop="durationDays" label="时长（天）" width="120" />
        <el-table-column label="权益" min-width="260">
          <template #default="scope">
            {{ scope.row.benefitList.join('、') }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="packageStatusTagMap[scope.row.status]">{{ packageStatusTextMap[scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortNo" label="排序" width="80" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">切换状态</el-button>
            <el-button link type="danger" @click="removePackage(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchPackages" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="currentPackageId ? '编辑套餐' : '新增套餐'" width="560px">
      <el-form :model="formData" label-width="88px">
        <el-form-item label="套餐名称">
          <el-input v-model="formData.packageName" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="formData.price" :min="0" :max="9999" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="时长天数">
          <el-input-number v-model="formData.durationDays" :min="1" :max="3650" style="width: 100%" />
        </el-form-item>
        <el-form-item label="权益列表">
          <el-input v-model="benefitText" placeholder="使用中文逗号分隔权益，如：课程折扣，专属客服" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formData.status" style="width: 100%">
            <el-option label="启用" value="enabled" />
            <el-option label="禁用" value="disabled" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortNo" :min="1" :max="99" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePackage">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { memberPackageHttp } from '@/api/modules/memberPackages';
import { PACKAGE_STATUS_OPTIONS, PACKAGE_STATUS_TAG_MAP, PACKAGE_STATUS_TEXT_MAP } from '@/constants/enums';
import type { MemberPackageRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, normalizePageList } from '@/utils/table';
import { confirmDangerAction } from '@/utils/confirm';

const loading = ref(false);
const dialogVisible = ref(false);
const total = ref(0);
const currentPackageId = ref<number | null>(null);
const tableData = ref<MemberPackageRecord[]>([]);
const pageLayout = DEFAULT_PAGE_LAYOUT;

const packageStatusOptions = PACKAGE_STATUS_OPTIONS;
const packageStatusTextMap = PACKAGE_STATUS_TEXT_MAP;
const packageStatusTagMap = PACKAGE_STATUS_TAG_MAP;

const query = reactive(createPageQuery({
  status: 'all',
}));

const defaultForm = (): MemberPackageRecord => ({
  packageId: 0,
  packageName: '',
  price: 29.9,
  durationDays: 30,
  benefitList: [],
  status: 'enabled',
  sortNo: 1,
});

const formData = reactive(defaultForm());

const benefitText = computed({
  get: () => formData.benefitList.join('，'),
  set: value => {
    formData.benefitList = value
      .split(/[，,]/)
      .map(item => item.trim())
      .filter(Boolean);
  },
});

const fetchPackages = async () => {
  loading.value = true;
  try {
    const res = await memberPackageHttp.getPackageList({ params: query });
    const pageResult = normalizePageList<MemberPackageRecord>(res.data, query.pageNum, query.pageSize);
    tableData.value = pageResult.list;
    total.value = pageResult.total;
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.status = 'all';
  query.pageNum = 1;
  fetchPackages();
};

const openDialog = (row: MemberPackageRecord | null) => {
  currentPackageId.value = row?.packageId || null;
  Object.assign(formData, row || defaultForm());
  dialogVisible.value = true;
};

const savePackage = async () => {
  await memberPackageHttp.savePackage({ data: formData });
  ElMessage.success(currentPackageId.value ? '套餐已更新' : '套餐已创建');
  dialogVisible.value = false;
  fetchPackages();
};

const toggleStatus = async (row: MemberPackageRecord) => {
  const status: MemberPackageRecord['status'] = row.status === 'enabled' ? 'disabled' : 'enabled';
  await memberPackageHttp.savePackage({ data: { ...row, status } });
  ElMessage.success(`套餐状态已更新为${packageStatusTextMap[status]}`);
  fetchPackages();
};

const removePackage = (row: MemberPackageRecord) =>
  confirmDangerAction({
    message: `确认删除套餐 ${row.packageName} 吗？`,
    successMessage: `已删除套餐 ${row.packageName}`,
    onConfirm: () => memberPackageHttp.deletePackage(row.packageId),
    onSuccess: fetchPackages,
  });

onMounted(fetchPackages);
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
  grid-template-columns: 1fr auto;
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
