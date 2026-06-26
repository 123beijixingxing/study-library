<template>
  <div class="page-shell">
    <section class="page-card">
      <div class="toolbar-head">
        <div>
          <h1 class="page-title">系统管理</h1>
          <p class="page-desc">维护系统基础信息和关键功能开关，支持后台系统参数的集中配置。</p>
        </div>
        <el-button type="primary" @click="saveSystemInfo">保存系统信息</el-button>
      </div>

      <el-form :model="systemInfo" label-width="96px" class="system-form">
        <el-form-item label="系统名称">
          <el-input v-model="systemInfo.systemName" />
        </el-form-item>
        <el-form-item label="系统版本">
          <el-input v-model="systemInfo.version" />
        </el-form-item>
        <el-form-item label="版权信息">
          <el-input v-model="systemInfo.copyright" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="systemInfo.contactInfo" />
        </el-form-item>
      </el-form>
    </section>

    <section class="page-card">
      <div class="feature-head">
        <div>
          <h2 class="section-title">功能开关</h2>
          <p class="page-desc">用于管理扫一扫、离线下载、注册等关键功能是否开放。</p>
        </div>
        <el-button @click="fetchFeatureList">刷新开关</el-button>
      </div>

      <el-table :data="featureList" v-loading="loading" border>
        <el-table-column prop="name" label="功能名称" min-width="160" />
        <el-table-column prop="description" label="说明" min-width="320" />
        <el-table-column label="开关状态" width="120">
          <template #default="scope">
            <el-switch :model-value="scope.row.enabled" @change="toggleFeature(scope.row, $event)" />
          </template>
        </el-table-column>
      </el-table>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { systemHttp } from '@/api/modules/system';
import type { SystemFeatureRecord, SystemInfoRecord } from '@/types/admin';

const loading = ref(false);
const featureList = ref<SystemFeatureRecord[]>([]);
const systemInfo = reactive<SystemInfoRecord>({
  systemName: '',
  version: '',
  copyright: '',
  contactInfo: '',
});

const fetchSystemInfo = async () => {
  const res = await systemHttp.getSystemInfo();
  Object.assign(systemInfo, res.data);
};

const fetchFeatureList = async () => {
  loading.value = true;
  try {
    const res = await systemHttp.getFeatureList();
    featureList.value = res.data;
  } finally {
    loading.value = false;
  }
};

const saveSystemInfo = async () => {
  await systemHttp.updateSystemInfo({ data: systemInfo });
  ElMessage.success('系统信息已保存');
};

const toggleFeature = async (row: SystemFeatureRecord, enabled: boolean | string | number) => {
  await systemHttp.updateFeature({ data: { ...row, enabled: Boolean(enabled) } });
  ElMessage.success(`${row.name}已${enabled ? '启用' : '关闭'}`);
  fetchFeatureList();
};

onMounted(async () => {
  await Promise.all([fetchSystemInfo(), fetchFeatureList()]);
});
</script>

<style scoped lang="scss">
.toolbar-head,
.feature-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.system-form {
  margin-top: 18px;
  max-width: 720px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
}

@media (max-width: 960px) {
  .toolbar-head,
  .feature-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
