<template>
  <article class="overview-card">
    <div class="overview-title">{{ title }}</div>
    <div class="overview-value">{{ value }}</div>
    <div class="overview-desc">{{ description }}</div>
  </article>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  value: string;
  description: string;
}>();
</script>

<style scoped lang="scss">
.overview-card {
  padding: 20px;
  border-radius: 16px;
  background: linear-gradient(135deg, #ffffff 0%, #f8fbff 100%);
  border: 1px solid var(--admin-border);
}

.overview-title {
  font-size: 14px;
  color: var(--admin-text-muted);
}

.overview-value {
  margin-top: 12px;
  font-size: 30px;
  font-weight: 700;
  color: var(--admin-text);
}

.overview-desc {
  margin-top: 10px;
  font-size: 13px;
  color: var(--admin-text-muted);
}
</style>
