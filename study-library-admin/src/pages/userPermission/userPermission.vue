<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card head-block">
      <div>
        <h1 class="page-title">用户权限配置</h1>
        <p class="page-desc">为用户分配角色和权限项，演示后台权限配置页的结构和保存流程。</p>
      </div>
      <div class="head-actions">
        <el-button @click="backToDetail">查看详情</el-button>
        <el-button type="primary" @click="savePermission">保存权限</el-button>
      </div>
    </section>

    <section class="page-card">
      <el-form label-width="88px">
        <el-form-item label="用户 ID">
          <el-input :model-value="String(userId)" disabled />
        </el-form-item>
        <el-form-item label="角色分配">
          <el-checkbox-group v-model="formData.roleList">
            <el-checkbox label="普通用户" />
            <el-checkbox label="VIP用户" />
            <el-checkbox label="管理员" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="权限项">
          <el-checkbox-group v-model="formData.permissionList" class="permission-grid">
            <el-checkbox v-for="item in permissionOptions" :key="item" :label="item" />
          </el-checkbox-group>
        </el-form-item>
      </el-form>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { userHttp } from '@/api/modules/users';
import type { UserPermissionRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const userId = Number(route.params.id);

const permissionOptions = [
  'dashboard:view',
  'user:manage',
  'course:manage',
  'garden:manage',
  'member:manage',
  'message:manage',
  'service:manage',
  'system:manage',
  'statistics:view',
  'logs:view',
];

const formData = reactive<UserPermissionRecord>({
  userId,
  roleList: [],
  permissionList: [],
});

const fetchPermission = async () => {
  loading.value = true;
  try {
    const res = await userHttp.getUserPermission(userId);
    Object.assign(formData, res.data);
  } finally {
    loading.value = false;
  }
};

const savePermission = async () => {
  await userHttp.saveUserPermission(userId, { data: formData });
  ElMessage.success('权限配置已保存');
};

const backToDetail = () => {
  router.push(`/user-manage/${userId}/detail`);
};

onMounted(fetchPermission);
</script>

<style scoped lang="scss">
.head-block {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.head-actions {
  display: flex;
  gap: 12px;
}

.permission-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

@media (max-width: 960px) {
  .head-block {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
