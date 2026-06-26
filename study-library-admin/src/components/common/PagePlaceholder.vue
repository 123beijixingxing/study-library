<template>
  <div class="page-shell">
    <section class="page-card">
      <h1 class="page-title">{{ title }}</h1>
      <p class="page-desc">{{ description }}</p>
    </section>
    <section class="page-card" v-if="features.length">
      <h2 class="section-title">建议落地内容</h2>
      <ul class="feature-list">
        <li v-for="item in features" :key="item">{{ item }}</li>
      </ul>
    </section>
  </div>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    title: string;
    description: string;
    features?: string[];
  }>(),
  {
    features: () => [],
  }
);
</script>

<style scoped lang="scss">
.section-title {
  font-size: 18px;
  font-weight: 600;
}

.feature-list {
  margin-top: 12px;
  display: grid;
  gap: 10px;
  color: var(--admin-text-muted);
}

.feature-list li {
  position: relative;
  padding-left: 16px;
}

.feature-list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 10px;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--admin-primary);
}
</style>
