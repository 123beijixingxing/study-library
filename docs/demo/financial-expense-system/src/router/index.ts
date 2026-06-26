import { createRouter, createWebHashHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';
import { beforeEachIntercept, afterEachIntercept } from '@/router/utils/permission'; // 权限控制

import login from '@/pages/login/login.vue';
// import base from '../utils/base';

// const queryString = base.getQueryStringInfo(window.location.search);
// console.log('queryString', window.location.search, queryString);
// Object.keys(queryString).forEach((key) => {

// })
console.log('window.location', window.location);
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: login,
  },
  {
    path: '/:catchAll(.*)',
    // path: "/:pathMatch(.*)*",
    name: 'login',
    component: login,
    // hidden: true,
  },
];

const router = createRouter({
  // history: createWebHistory(process.env.BASE_URL),
  history: createWebHashHistory(),
  // scrollBehavior: () => ({ y: 0 }),
  routes,
});

router.addRoute({
  path: '/login',
  name: 'login',
  component: login,
});

export default router;
beforeEachIntercept();
afterEachIntercept();
