<template>
  <div class="route-tabs">
    <div
      v-for="item in tabs"
      :key="item.path"
      :class="['route-tab', { 'route-tab--active': item.path === route.path }]"
      @click="goTab(item.path)">
      <span>{{ item.localeKey ? t(item.localeKey) : item.title }}</span>
      <button v-if="item.closable" type="button" class="close-btn" @click.stop="closeTab(item.path)">x</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { useI18n } from 'vue-i18n';
import type { VisitedTab } from '@/types/app';

const route = useRoute();
const router = useRouter();
const store = useStore();
const { t } = useI18n();

const tabs = computed(() => store.getters.visitedTabs as VisitedTab[]);

const goTab = (path: string) => {
  if (path !== route.path) {
    router.push(path);
  }
};

const closeTab = (path: string) => {
  const currentPath = route.path;
  const nextTabs = tabs.value.filter(item => item.path !== path);
  store.dispatch('removeVisitedTab', path);

  if (currentPath === path) {
    const next = nextTabs[nextTabs.length - 1];
    router.push(next?.path || '/dashboard');
  }
};
</script>

<style scoped lang="scss">
.route-tabs {
  display: flex;
  gap: 10px;
  overflow-x: auto;
}

.route-tab {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  border-radius: 12px;
  border: 1px solid var(--admin-border);
  background: #ffffff;
  color: var(--admin-text-muted);
  cursor: pointer;
  white-space: nowrap;
}

.route-tab--active {
  border-color: var(--admin-primary);
  background: var(--admin-primary-soft);
  color: var(--admin-primary);
}

.close-btn {
  border: none;
  background: transparent;
  color: currentColor;
  cursor: pointer;
}
</style>
