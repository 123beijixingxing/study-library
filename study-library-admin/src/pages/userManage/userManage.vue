<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">用户管理</h1>
          <p class="page-desc">维护后台用户列表、角色、状态与基础资料，并为后续用户详情和权限配置提供入口。</p>
        </div>
        <div class="toolbar-actions">
          <el-button @click="handleImport">导入</el-button>
          <el-button @click="handleExport">导出</el-button>
          <el-button type="primary" @click="openCreateDialog">新增用户</el-button>
        </div>
      </div>

      <div class="filter-grid">
        <el-input v-model="query.keyword" placeholder="搜索用户名 / 邮箱 / 手机号" clearable />
        <el-select v-model="query.role" placeholder="角色筛选">
          <el-option v-for="item in userRoleOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="query.status" placeholder="状态筛选">
          <el-option v-for="item in userStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <div class="filter-actions">
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="fetchUsers">查询</el-button>
        </div>
      </div>
    </section>

    <section class="page-card">
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column label="#" width="60">
          <template #default="scope">
            {{ getRowIndex(query.pageNum, query.pageSize, scope.$index) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="140" />
        <el-table-column prop="email" label="邮箱" min-width="220" />
        <el-table-column prop="phone" label="手机号" min-width="160" />
        <el-table-column prop="role" label="角色" min-width="120" />
        <el-table-column label="状态" width="110">
          <template #default="scope">
            <el-tag :type="userStatusTagMap[scope.row.status]">
              {{ userStatusTextMap[scope.row.status] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="registerTime" label="注册时间" min-width="180" />
        <el-table-column label="操作" width="330" fixed="right">
          <template #default="scope">
            <el-button link @click="openDetailPage(scope.row)">详情</el-button>
            <el-button link type="primary" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button link @click="toggleStatus(scope.row)">{{ scope.row.status === 'enabled' ? '禁用' : '启用' }}</el-button>
            <el-button link type="warning" @click="openPermissionPage(scope.row)">权限</el-button>
            <el-button link type="danger" @click="removeUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :layout="pageLayout"
          :total="total"
          @current-change="fetchUsers" />
      </div>
    </section>

    <el-dialog v-model="dialogVisible" :title="dialogMode === 'create' ? '新增用户' : '编辑用户'" width="520px">
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="88px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" :disabled="dialogMode === 'edit'" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="formData.role" style="width: 100%">
            <el-option label="普通用户" value="普通用户" />
            <el-option label="VIP用户" value="VIP用户" />
            <el-option label="管理员" value="管理员" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio label="enabled">启用</el-radio>
            <el-radio label="disabled">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { USER_ROLE_OPTIONS, USER_STATUS_OPTIONS, USER_STATUS_TAG_MAP, USER_STATUS_TEXT_MAP } from '@/constants/enums';
import { userHttp } from '@/api/modules/users';
import type { UserRecord } from '@/types/admin';
import { DEFAULT_PAGE_LAYOUT, createPageQuery, getRowIndex } from '@/utils/table';
import { confirmDangerAction } from '@/utils/confirm';

const router = useRouter();
const loading = ref(false);
const total = ref(0);
const tableData = ref<UserRecord[]>([]);
const dialogVisible = ref(false);
const dialogMode = ref<'create' | 'edit'>('create');
const currentUserId = ref<number | null>(null);
const formRef = ref<FormInstance>();

const pageLayout = DEFAULT_PAGE_LAYOUT;
const userRoleOptions = USER_ROLE_OPTIONS;
const userStatusOptions = USER_STATUS_OPTIONS;
const userStatusTextMap = USER_STATUS_TEXT_MAP;
const userStatusTagMap = USER_STATUS_TAG_MAP;

const query = reactive(createPageQuery({
  keyword: '',
  role: 'all',
  status: 'all',
}));

const defaultForm = () => ({
  username: '',
  email: '',
  phone: '',
  role: '普通用户',
  status: 'enabled' as UserRecord['status'],
});

const formData = reactive(defaultForm());

const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }],
};

const fetchUsers = async () => {
  loading.value = true;
  try {
    const res = await userHttp.getUserList({ params: query });
    tableData.value = res.data.list;
    total.value = res.data.total;
  } finally {
    loading.value = false;
  }
};

const resetQuery = () => {
  query.keyword = '';
  query.role = 'all';
  query.status = 'all';
  query.pageNum = 1;
  fetchUsers();
};

const openCreateDialog = () => {
  dialogMode.value = 'create';
  currentUserId.value = null;
  Object.assign(formData, defaultForm());
  dialogVisible.value = true;
};

const openEditDialog = (row: UserRecord) => {
  dialogMode.value = 'edit';
  currentUserId.value = row.id;
  Object.assign(formData, row);
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!formRef.value) {
    return;
  }

  await formRef.value.validate();

  if (dialogMode.value === 'create') {
    await userHttp.createUser({ data: formData });
    ElMessage.success('用户已创建');
  } else if (currentUserId.value) {
    await userHttp.updateUser(currentUserId.value, { data: formData });
    ElMessage.success('用户信息已更新');
  }

  dialogVisible.value = false;
  fetchUsers();
};

const toggleStatus = async (row: UserRecord) => {
  const status = row.status === 'enabled' ? 'disabled' : 'enabled';
  await userHttp.updateUserStatus(row.id, { data: { status } });
  ElMessage.success(status === 'enabled' ? '已启用用户' : '已禁用用户');
  fetchUsers();
};

const openPermissionPage = (row: UserRecord) => {
  router.push(`/user-manage/${row.id}/permission`);
};

const openDetailPage = (row: UserRecord) => {
  router.push(`/user-manage/${row.id}/detail`);
};

const removeUser = (row: UserRecord) =>
  confirmDangerAction({
    message: `确认删除用户 ${row.username} 吗？`,
    successMessage: `已删除 ${row.username}`,
    onConfirm: () => userHttp.deleteUser(row.id),
    onSuccess: fetchUsers,
  });

const handleImport = () => {
  ElMessage.info('导入流程待接入 Excel 上传接口');
};

const handleExport = () => {
  ElMessage.success('已触发导出任务（当前为演示反馈）');
};

onMounted(fetchUsers);
</script>

<style scoped lang="scss">
.toolbar-head,
.filter-grid,
.toolbar-actions,
.filter-actions {
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
  grid-template-columns: 2fr 1fr 1fr auto;
}

.pagination-wrap {
  margin-top: 20px;
  display: flex;
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
