<template>
  <div class="page-shell" v-loading="loading">
    <section class="page-card detail-head">
      <div>
        <h1 class="page-title">用户详情</h1>
        <p class="page-desc">查看用户基础资料、会员状态、学习记录摘要和收藏情况，便于后台运营快速判断用户状态。</p>
      </div>
      <div class="head-actions">
        <el-button @click="goPermission">权限配置</el-button>
        <el-button type="primary" @click="backToList">返回用户列表</el-button>
      </div>
    </section>

    <section class="overview-grid" v-if="detail">
      <OverviewCard title="用户名" :value="detail.username" description="后台用户唯一登录名" />
      <OverviewCard title="会员等级" :value="detail.memberLevel" description="用户当前会员体系状态" />
      <OverviewCard title="学习记录" :value="`${detail.studyRecordCount}`" description="累计学习记录数量" />
      <OverviewCard title="收藏数量" :value="`${detail.favoriteCount}`" description="用户收藏课程或内容数" />
    </section>

    <section class="page-card" v-if="detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户名">{{ detail.username }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ detail.role }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detail.email }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.phone }}</el-descriptions-item>
        <el-descriptions-item label="账号状态">{{ detail.status === 'enabled' ? '启用' : '禁用' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ detail.registerTime }}</el-descriptions-item>
        <el-descriptions-item label="最近登录时间">{{ detail.lastLoginTime }}</el-descriptions-item>
      </el-descriptions>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import OverviewCard from '@/components/business/OverviewCard.vue';
import { userHttp } from '@/api/modules/users';
import type { UserDetailRecord } from '@/types/admin';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const detail = ref<UserDetailRecord | null>(null);

const userId = Number(route.params.id);

const fetchDetail = async () => {
  loading.value = true;
  try {
    const res = await userHttp.getUserDetail(userId);
    detail.value = res.data;
  } finally {
    loading.value = false;
  }
};

const backToList = () => {
  router.push('/user-manage');
};

const goPermission = () => {
  router.push(`/user-manage/${userId}/permission`);
};

onMounted(fetchDetail);
</script>

<style scoped lang="scss">
.detail-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.head-actions {
  display: flex;
  gap: 12px;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
}

@media (max-width: 960px) {
  .detail-head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
