import router from '@/router';
import routerConfig from '@/router/utils/routerConfig';
// import store from '@/store/index';
import base from '@/utils/base';

const routerData: any[] = [];

Object.keys(routerConfig.getRouterData()).map(key => {
  const item = routerConfig.getRouterData()[key];
  item?.config && routerData.push(routerConfig.getRouterData()[key]?.config);
});

const flatRouterData: any[] = base.flatMapTreeArray(routerData, [], '');
const allRouterPath = flatRouterData.map(item => item.path);

console.log('routerConfig', routerConfig, routerData, flatRouterData, allRouterPath);

const { origin, pathname, hash, search } = window.location;
const newPageUrl = `${origin}${pathname}${hash}`;
const queryString = base.getQueryStringInfo(search);
console.log('queryString', search, queryString, newPageUrl, hash);
console.log('origin, pathname, hash, search', origin, pathname, hash, search);
if (!hash && queryString) {
  location.assign(newPageUrl + `#/${search}`);
}

const nMV = (to: any) => {
  return ['', '/'].includes(to.path);
};
const lGV = (to: any) => {
  return ['/login'].includes(to.path);
};
const aRMV = (to: any) => {
  return allRouterPath.includes(to.path);
};

export const beforeEachIntercept = () => {
  // 全局路由守卫拦截处理
  router.beforeEach((to, from, next) => {
    const query = Object.keys(from.query).length ? from.query : to.query;
    const params = Object.keys(from.params).length ? from.params : to.params;
    if (!aRMV(to) && nMV(to)) {
      next({ name: 'login', query, params });
      // router.replace({ path: '/login', query: queryString });
    } else {
      next();
    }
    console.log('beforeEach', to, from, next);
  });
};
export const afterEachIntercept = () => {
  // 全局路由守卫拦截处理
  router.afterEach((to, from, failure) => {
    console.log('afterEach', to, from, failure);
  });
};

// 全局路由守卫拦截处理
// router.beforeEach((to, from, next) => {
//   console.log('beforeEach', to, from, next);
// });
// router.afterEach((to, from, failure) => {
//   console.log('afterEach', to, from, failure);
// });
