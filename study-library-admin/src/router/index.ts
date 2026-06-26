import { createRouter, createWebHashHistory } from 'vue-router';
import i18n from '@/languageApi';
import AdminLayout from '@/layout/AdminLayout.vue';
import { adminMenuRoutes } from '@/router/utils/routerConfig';
import { setupPermissionGuard } from '@/router/utils/permission';
import store from '@/store';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/pages/login/login.vue'),
      meta: { title: '登录', localeKey: 'login.title', requiresAuth: false },
    },
    {
      path: '/',
      component: AdminLayout,
      redirect: '/dashboard',
      meta: { requiresAuth: true },
      children: [
        ...adminMenuRoutes.map(item => ({
          path: item.path.replace(/^\//, ''),
          name: item.name,
          component: item.component,
          meta: {
            title: item.title,
            localeKey: item.localeKey,
            icon: item.icon,
            keepAlive: item.keepAlive ?? false,
            requiresAuth: true,
          },
        })),
        {
          path: 'user-manage/:id/detail',
          name: 'userDetail',
          component: () => import('@/pages/userDetail/userDetail.vue'),
          meta: { title: '用户详情', requiresAuth: true },
        },
        {
          path: 'user-manage/:id/permission',
          name: 'userPermission',
          component: () => import('@/pages/userPermission/userPermission.vue'),
          meta: { title: '用户权限配置', requiresAuth: true },
        },
        {
          path: 'course-manage/:id/detail',
          name: 'courseDetailManage',
          component: () => import('@/pages/courseDetailManage/courseDetailManage.vue'),
          meta: { title: '课程详情管理', requiresAuth: true },
        },
        {
          path: 'garden-manage/hot-audit',
          name: 'hotContentAudit',
          component: () => import('@/pages/hotContentAudit/hotContentAudit.vue'),
          meta: { title: '火热内容审核', requiresAuth: true },
        },
        {
          path: 'garden-manage/share-audit',
          name: 'shareContentAudit',
          component: () => import('@/pages/shareContentAudit/shareContentAudit.vue'),
          meta: { title: '分享内容审核', requiresAuth: true },
        },
        {
          path: 'course-manage/evaluations',
          name: 'courseEvaluationManage',
          component: () => import('@/pages/courseEvaluationManage/courseEvaluationManage.vue'),
          meta: { title: '课程评价管理', requiresAuth: true },
        },
        {
          path: 'member-manage/study-records',
          name: 'studyRecordManage',
          component: () => import('@/pages/studyRecordManage/studyRecordManage.vue'),
          meta: { title: '学习记录管理', requiresAuth: true },
        },
        {
          path: 'question-bank-manage/:id/questions',
          name: 'questionManage',
          component: () => import('@/pages/questionManage/questionManage.vue'),
          meta: { title: '题目管理', requiresAuth: true },
        },
        {
          path: 'question-bank-manage/:id/questions/:questionId/detail',
          name: 'questionDetailManage',
          component: () => import('@/pages/questionDetailManage/questionDetailManage.vue'),
          meta: { title: '题目详情配置', requiresAuth: true },
        },
        {
          path: 'question-bank-manage/:id/questions/:questionId/audit',
          name: 'questionVersionAuditCenter',
          component: () => import('@/pages/questionVersionAuditCenter/questionVersionAuditCenter.vue'),
          meta: { title: '版本审计总台', requiresAuth: true },
        },
        {
          path: 'practice-paper-manage/:id/detail',
          name: 'practicePaperDetailManage',
          component: () => import('@/pages/practicePaperDetailManage/practicePaperDetailManage.vue'),
          meta: { title: '试卷明细与分析', requiresAuth: true },
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/dashboard',
    },
  ],
});

setupPermissionGuard(router);

router.afterEach(to => {
  const pageTitle = typeof to.meta.localeKey === 'string' ? i18n.global.t(to.meta.localeKey) : typeof to.meta.title === 'string' ? to.meta.title : '后台管理';
  document.title = `${pageTitle} - ${i18n.global.t('app.name')}`;

  if (to.path !== '/login' && typeof to.name === 'string' && typeof to.meta.title === 'string') {
    store.dispatch('addVisitedTab', {
      path: to.path,
      name: to.name,
      title: to.meta.title,
      localeKey: typeof to.meta.localeKey === 'string' ? to.meta.localeKey : undefined,
      closable: to.path !== '/dashboard',
    });
  }
});

export default router;
