<template>
  <el-breadcrumb separator="/" class="breadcrumb-bar">
    <el-breadcrumb-item>{{ t('layout.home') }}</el-breadcrumb-item>
    <el-breadcrumb-item v-for="item in items" :key="item.path || item.title">
      {{ item.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { useI18n } from 'vue-i18n';

const route = useRoute();
const { t } = useI18n();

const items = computed(() =>
  route.matched
    .filter(item => item.path !== '/' && item.meta?.title)
    .map(item => ({
      path: item.path,
      title: item.meta?.localeKey ? t(item.meta.localeKey as string) : (item.meta?.title as string),
    }))
);
</script>

<style scoped lang="scss">
.breadcrumb-bar {
  margin-top: 6px;
}
</style>
