import type { Router } from 'vue-router';
import store from '@/store';

export const setupPermissionGuard = (router: Router) => {
  router.beforeEach(to => {
    const token = store.getters.token as string;
    const requiresAuth = to.meta.requiresAuth !== false;

    if (to.path === '/login' && token) {
      return '/dashboard';
    }

    if (requiresAuth && !token) {
      return {
        path: '/login',
        query: to.fullPath ? { redirect: to.fullPath } : {},
      };
    }

    return true;
  });
};
