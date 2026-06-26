<template>
  <el-container class="layout-shell">
    <el-aside :width="isCollapse ? '84px' : '240px'" class="layout-aside">
      <div class="brand-block">
        <div class="brand-mark">ST</div>
        <div v-if="!isCollapse" class="brand-copy">
          <div class="brand-title">{{ t('app.name') }}</div>
          <div class="brand-subtitle">Admin Console</div>
        </div>
      </div>

      <el-scrollbar>
        <el-menu :default-active="route.path" :collapse="isCollapse" router class="menu-panel">
          <el-menu-item v-for="item in adminMenuRoutes" :key="item.path" :index="item.path">
            <MenuIcon :icon="item.icon" :title="item.title" />
            <span>{{ t(item.localeKey) }}</span>
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header class="layout-header">
        <div>
          <el-button text @click="toggleSidebar">{{ isCollapse ? t('layout.expandMenu') : t('layout.collapseMenu') }}</el-button>
          <h1 class="header-title">{{ currentTitle }}</h1>
        </div>
        <div class="header-actions">
          <el-select v-model="currentLanguage" class="language-select" @change="changeLanguage">
            <el-option v-for="item in languageOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <span class="user-name">{{ userName }}</span>
          <el-button type="primary" plain @click="logout">{{ t('layout.logout') }}</el-button>
        </div>
      </el-header>

      <el-main class="layout-main">
        <div class="layout-tools">
          <BreadcrumbBar />
          <RouteTabs />
        </div>
        <router-view v-slot="{ Component, route: currentRoute }">
          <keep-alive>
            <component :is="Component" v-if="currentRoute.meta.keepAlive" />
          </keep-alive>
          <component :is="Component" v-if="!currentRoute.meta.keepAlive" />
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { useStore } from 'vuex';
import { authHttp } from '@/api/modules/auth';
import BreadcrumbBar from '@/components/common/BreadcrumbBar.vue';
import MenuIcon from '@/components/common/MenuIcon.vue';
import RouteTabs from '@/components/common/RouteTabs.vue';
import { LANGUAGE_OPTIONS } from '@/constants/enums';
import { adminMenuRoutes } from '@/router/utils/routerConfig';

const route = useRoute();
const router = useRouter();
const store = useStore();
const { t, locale } = useI18n();

const isCollapse = computed(() => store.getters.sidebarCollapsed as boolean);
const userName = computed(() => (store.getters.userInfo?.username as string) || '管理员');
const currentLanguage = computed({
  get: () => locale.value,
  set: value => {
    locale.value = value;
  },
});
const languageOptions = LANGUAGE_OPTIONS;
const currentTitle = computed(() => {
  if (typeof route.meta.localeKey === 'string') {
    return t(route.meta.localeKey);
  }
  return (route.meta.title as string) || t('layout.home');
});

const toggleSidebar = () => {
  store.dispatch('setSidebarCollapsed', !isCollapse.value);
};

const changeLanguage = (value: string) => {
  locale.value = value;
  const selected = languageOptions.find(item => item.value === value);
  store.dispatch('setLanguageType', {
    name: selected?.label || value,
    type: value,
  });
};

const logout = async () => {
  await authHttp.logout().catch(() => undefined);
  await store.dispatch('clearLogin');
  await store.dispatch('resetVisitedTabs');
  router.push('/login');
};
</script>

<style scoped lang="scss">
.layout-shell {
  width: 100%;
  height: 100%;
}

.layout-aside {
  display: flex;
  flex-direction: column;
  border-right: 1px solid var(--admin-border);
  background: #111827;
  color: #f9fafb;
}

.brand-block {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.brand-mark {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: linear-gradient(135deg, #2563eb 0%, #0ea5e9 100%);
  font-weight: 700;
}

.brand-title {
  font-size: 16px;
  font-weight: 700;
}

.brand-subtitle {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.65);
}

.menu-panel {
  border-right: none;
  background: transparent;
}

.menu-panel :deep(.el-menu-item) {
  color: rgba(255, 255, 255, 0.88);
}

.menu-panel :deep(.el-menu-item.is-active) {
  background: rgba(37, 99, 235, 0.18);
  color: #ffffff;
}

.layout-header {
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #ffffff;
  border-bottom: 1px solid var(--admin-border);
}

.layout-header > div:first-child {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-title {
  font-size: 20px;
  font-weight: 700;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.language-select {
  width: 120px;
}

.user-name {
  color: var(--admin-text-muted);
}

.layout-main {
  padding: 24px;
  background: var(--admin-bg);
}

.layout-tools {
  margin-bottom: 18px;
  display: grid;
  gap: 12px;
}
</style>
