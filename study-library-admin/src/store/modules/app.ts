import Storage from '@/utils/saveStorage';
import type { VisitedTab } from '@/types/app';

const defaultTabs: VisitedTab[] = [
  {
    path: '/dashboard',
    name: 'dashboard',
    title: '仪表盘',
    localeKey: 'menu.dashboard',
    closable: false,
  },
];

const appModule = {
  state: {
    sidebarCollapsed: Storage.readSession<boolean>('admin_sidebar_collapsed') || false,
    languageType: Storage.readSession<{ name: string; type: string }>('admin_language_type') || { name: '中文', type: 'zh' },
    visitedTabs: Storage.readSession<VisitedTab[]>('admin_visited_tabs') || defaultTabs,
  },
  mutations: {
    setSidebarCollapsed(state: { sidebarCollapsed: boolean }, value: boolean) {
      state.sidebarCollapsed = value;
      Storage.saveSession('admin_sidebar_collapsed', value);
    },
    setLanguageType(state: { languageType: { name: string; type: string } }, value: { name: string; type: string }) {
      state.languageType = value;
      Storage.saveSession('admin_language_type', value);
    },
    addVisitedTab(state: { visitedTabs: VisitedTab[] }, value: VisitedTab) {
      const exists = state.visitedTabs.some(item => item.path === value.path);
      if (!exists) {
        state.visitedTabs.push(value);
        Storage.saveSession('admin_visited_tabs', state.visitedTabs);
      }
    },
    removeVisitedTab(state: { visitedTabs: VisitedTab[] }, path: string) {
      state.visitedTabs = state.visitedTabs.filter(item => !item.closable || item.path !== path);
      Storage.saveSession('admin_visited_tabs', state.visitedTabs);
    },
    resetVisitedTabs(state: { visitedTabs: VisitedTab[] }) {
      state.visitedTabs = [...defaultTabs];
      Storage.saveSession('admin_visited_tabs', state.visitedTabs);
    },
  },
  actions: {
    setSidebarCollapsed({ commit }: { commit: Function }, value: boolean) {
      commit('setSidebarCollapsed', value);
    },
    setLanguageType({ commit }: { commit: Function }, value: { name: string; type: string }) {
      commit('setLanguageType', value);
    },
    addVisitedTab({ commit }: { commit: Function }, value: VisitedTab) {
      commit('addVisitedTab', value);
    },
    removeVisitedTab({ commit }: { commit: Function }, path: string) {
      commit('removeVisitedTab', path);
    },
    resetVisitedTabs({ commit }: { commit: Function }) {
      commit('resetVisitedTabs');
    },
  },
};

export default appModule;
